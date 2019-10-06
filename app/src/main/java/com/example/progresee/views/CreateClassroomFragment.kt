package com.example.progresee.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.progresee.R
import com.example.progresee.data.AppRepository
import com.example.progresee.viewmodels.ClassroomViewModel
import com.example.progresee.viewmodels.CreateClassroomViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CreateClassroomFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private val classroomViewModel: CreateClassroomViewModel by viewModel { parametersOf(appRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_classroom, container, false)
    }


}
