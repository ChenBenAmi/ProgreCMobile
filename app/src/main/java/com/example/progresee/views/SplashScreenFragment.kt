package com.example.progresee.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.example.progresee.databinding.FragmentSplashScreenBinding
import com.example.progresee.viewmodels.SplashState
import com.example.progresee.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenFragment : Fragment() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSplashScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false)

        binding.lifecycleOwner=this

        binding.splashViewModel=splashViewModel

        splashViewModel.liveData.observe(this, Observer {
            when (it) {
                is SplashState.MainActivity -> {
                }
            }
        })

        splashViewModel.navigateToLoginFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
                splashViewModel.doneNavigating()
            }
        })

        return binding.root
    }

}
