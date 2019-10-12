package com.example.progresee.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentHomeBinding
import com.example.progresee.viewmodels.HomeViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class HomeFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private val homeViewModel: HomeViewModel by viewModel { parametersOf(appRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.app_name)
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        binding.homeViewModel = homeViewModel
        setHasOptionsMenu(true)
        binding.lifecycleOwner = this


        homeViewModel.navigateToFirebaseLoginFragment.observe(this, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                homeViewModel.doneNavigating()
            }

        })
        return binding.root
    }


}
