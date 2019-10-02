package com.example.progresee.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.progresee.R
import com.example.progresee.databinding.FragmentLoginBinding
import com.example.progresee.viewmodels.LoginViewModel
import com.example.progresee.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : Fragment() {


    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)


        binding.logViewModel = loginViewModel

        binding.lifecycleOwner = this


        loginViewModel.navigateToClassRoomFragment.observe(this, Observer {
            if (it==true) {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToClassroomFragment())
                loginViewModel.doneNavigating()
            }

        })
        return binding.root
    }


}
