package com.example.progresee.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.progresee.R
import com.example.progresee.adapters.ClassroomAdapter
import com.example.progresee.adapters.ClassroomClickListener
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentClassroomBinding
import com.example.progresee.viewmodels.ClassroomViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ClassroomFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private val classroomViewModel: ClassroomViewModel by viewModel { parametersOf(appRepository) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

       val binding:FragmentClassroomBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_classroom,container,false)

        binding.classroomViewModel = classroomViewModel

        val manager = LinearLayoutManager(context)
        binding.classroomList.layoutManager = manager

        val adapter = ClassroomAdapter(ClassroomClickListener { classroomId ->
            classroomViewModel.onClassroomClicked(classroomId)
        })
        binding.classroomList.adapter = adapter
        binding.lifecycleOwner = this
//        classroomViewModel.insertDummyData()

        classroomViewModel.classrooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        return binding.root

    }


}
