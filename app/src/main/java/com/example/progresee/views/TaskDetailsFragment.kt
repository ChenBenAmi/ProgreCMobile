package com.example.progresee.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresee.R
import com.example.progresee.adapters.TaskAdapter
import com.example.progresee.adapters.TaskClickListener
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentTaskDetailsBinding
import com.example.progresee.viewmodels.TaskDetailsViewModel
import com.example.progresee.viewmodels.TaskViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ExerciseFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTaskDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_details, container, false)

        binding.lifecycleOwner = this

        val arguments = TaskFragmentArgs.fromBundle(arguments!!)
        val taskDetailsViewModel: TaskDetailsViewModel by viewModel {
            parametersOf(
                appRepository,
                arguments.classroomId
            )
        }
        binding.taskDetailsViewModel = taskDetailsViewModel
        val manager = LinearLayoutManager(context)
        binding.exerciseList.layoutManager = manager
        val adapter = TaskAdapter(TaskClickListener { exerciseId ->
            taskDetailsViewModel.(taskId)
        })
        binding.exerciseList.adapter = adapter


        return binding.root
    }
}
