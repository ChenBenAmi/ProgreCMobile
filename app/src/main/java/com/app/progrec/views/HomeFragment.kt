package com.app.progrec.views


import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.progrec.R
import com.app.progrec.data.AppRepository
import com.app.progrec.databinding.FragmentHomeBinding
import com.app.progrec.viewmodels.HomeViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import android.content.Intent as Intent1


class HomeFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private val homeViewModel: HomeViewModel by viewModel { parametersOf(appRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        binding.homeViewModel = homeViewModel
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this




        homeViewModel.navigateToFirebaseLoginFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                homeViewModel.onDoneNavigating()
            }

        })
        return binding.root
    }



}
