package com.example.progresee.views


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.IdpResponse.fromResultIntent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber


class FirebaseLogin : Fragment() {

    private val RC_SIGN_IN = 123
    val auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_name)
        val auth = FirebaseAuth.getInstance()
        val cureentUser: FirebaseUser?=auth.currentUser
        if (auth.currentUser != null) {
            Timber.wtf(cureentUser?.displayName)

            this.findNavController().navigate(FirebaseLoginDirections.actionFirebaseLoginToClassroomFragment())
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firebase_login, container, false)
    }

    private fun showSnackBar(id: Int) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            "yay",
            Snackbar.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response: IdpResponse? = fromResultIntent(data)
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                this.findNavController().navigate(FirebaseLoginDirections.actionFirebaseLoginToClassroomFragment())
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
