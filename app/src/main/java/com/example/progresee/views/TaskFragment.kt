package com.example.progresee.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.progresee.R
import com.example.progresee.adapters.TaskAdapter
import com.example.progresee.adapters.TaskClickListener
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentTaskBinding
import com.example.progresee.viewmodels.TaskViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class TaskFragment : Fragment() {

    private val appRepository: AppRepository by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)



        binding.lifecycleOwner = this


        val argumnets = TaskFragmentArgs.fromBundle(arguments!!)


        val taskViewModel: TaskViewModel by viewModel {
            parametersOf(
                appRepository,
                argumnets.classroomId
            )
        }


        binding.taskViewModel = taskViewModel

        val manager = LinearLayoutManager(context)
        binding.taskList.layoutManager = manager
        val adapter = TaskAdapter(TaskClickListener { taskId ->
            taskViewModel.onTaskClicked(taskId)
        })
        binding.taskList.adapter = adapter
        //taskViewModel.insertDummyData()

        taskViewModel.getClassroomName().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.supportActionBar?.title =
                    taskViewModel.getClassroomName().value!!.name
            }
        })


        taskViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        taskViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {

            }
        })
        return binding.root

    }


}
