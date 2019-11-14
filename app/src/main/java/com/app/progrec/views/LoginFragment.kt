package com.app.progrec.views


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.progrec.R
import com.app.progrec.data.AppRepository
import com.app.progrec.viewmodels.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.IdpResponse.fromResultIntent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class LoginFragment : Fragment() {

    private val RC_SIGN_IN = 123
    private val appRepository: AppRepository by inject()
    private val loginViewModel: LoginViewModel by viewModel { parametersOf(appRepository) }
    private val auth = FirebaseAuth.getInstance()
    private val currentUser: FirebaseUser? = auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (currentUser != null) {
            this.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToClassroomFragment())
        } else {
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.EmailBuilder().build()
                    )
                ).setIsSmartLockEnabled(false)
                    .build(),
                RC_SIGN_IN
            )
        }
        loginViewModel.navigateToClassroomFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToClassroomFragment())
            }
        })

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun showSnackBar(id: Int) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            context!!.getString(id),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response: IdpResponse? = fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                this.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToClassroomFragment())
            } else {
                if (response == null) {
                    showSnackBar(R.string.sign_in_cancelled)
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    showSnackBar(R.string.no_internet_connection)
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    return
                }
                showSnackBar(R.string.unknown_error)
            }
        }
    }

}
