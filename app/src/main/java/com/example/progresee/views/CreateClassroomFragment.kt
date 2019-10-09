package com.example.progresee.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentCreateClassroomBinding
import com.example.progresee.viewmodels.CreateClassroomViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CreateClassroomFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private val createClassroomViewModel: CreateClassroomViewModel by viewModel {
        parametersOf(
            appRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            context?.getString(R.string.create_classroom_title)
        val binding: FragmentCreateClassroomBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_classroom, container, false)
        binding.lifecycleOwner = this
        binding.createClassroomViewModel = createClassroomViewModel

        createClassroomViewModel.navigateBackToClassroomFragment.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    this.findNavController()
                        .navigate(CreateClassroomFragmentDirections.actionCreateClassroomFragmentToClassroomFragment())
                    createClassroomViewModel.onDoneNavigating()
                }
            })

        createClassroomViewModel.stringLength.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.name_too_long),
                    Snackbar.LENGTH_LONG
                ).show()
                createClassroomViewModel.snackbarShown()
            }
        })


        return binding.root
    }


}
