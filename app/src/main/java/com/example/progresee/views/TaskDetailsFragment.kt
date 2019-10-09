package com.example.progresee.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresee.R
import com.example.progresee.adapters.ExerciseAdapter
import com.example.progresee.adapters.ExerciseClickListener
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentTaskDetailsBinding
import com.example.progresee.viewmodels.TaskDetailsViewModel
import kotlinx.android.synthetic.main.fragment_task_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class TaskDetailsFragment : Fragment() {

    private val appRepository: AppRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTaskDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_details, container, false)

        binding.lifecycleOwner = this

        val arguments = TaskDetailsFragmentArgs.fromBundle(arguments!!)
        val taskDetailsViewModel: TaskDetailsViewModel by viewModel {
            parametersOf(
                appRepository,
                arguments.taskId
            )
        }
        binding.taskDetailsViewModel = taskDetailsViewModel
        val manager = LinearLayoutManager(context)
        binding.exerciseList.layoutManager = manager
        val adapter = ExerciseAdapter(ExerciseClickListener { exerciseId ->
            taskDetailsViewModel.onTaskClicked(exerciseId)
        })
        binding.exerciseList.adapter = adapter

//        taskDetailsViewModel.insertDummyData()

        taskDetailsViewModel.exercises.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                exercises.text=context!!.getString(R.string.number_of_exercises,0,adapter.itemCount)
            }
        })

        taskDetailsViewModel.getTask().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.supportActionBar?.title =
                    taskDetailsViewModel.getTask().value!!.title
                binding.task=taskDetailsViewModel.getTask().value
            }
        })


        return binding.root
    }


}
