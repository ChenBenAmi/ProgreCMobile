package com.example.progresee.views


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.example.progresee.data.AppRepository
import com.example.progresee.viewmodels.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.IdpResponse.fromResultIntent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_classroom.*
import kotlinx.android.synthetic.main.fragment_create_classroom.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class LoginFragment : Fragment() {

    private val RC_SIGN_IN = 123
    val auth = FirebaseAuth.getInstance()

    private val appRepository: AppRepository by inject()
    private val loginViewModel: LoginViewModel by viewModel { parametersOf(appRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_name)
        val auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            currentUser.getIdToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.wtf(it.result?.token)
                    loginViewModel.getCurrentUser(it.result?.token)
                }
            }

        } else {
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                        AuthUI.IdpConfig.EmailBuilder().build()
                    )
                ).build(),
                RC_SIGN_IN
            )
        }
        loginViewModel.navigateToClassroomFragment.observe(viewLifecycleOwner, Observer {
            if (it==true) {
                this.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToClassroomFragment())
            }
        })

        loginViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true)
                layout_progress_bar.visibility = View.VISIBLE
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
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                this.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginFragmentToClassroomFragment())
            } else {
                if (response == null) {
                    // User pressed back button
                    showSnackBar(R.string.sign_in_cancelled)
                    return
                }
                if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                    showSnackBar(R.string.no_internet_connection)
                    return
                }
                showSnackBar(R.string.unknown_error)
            }
        }
    }

}
