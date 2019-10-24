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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresee.R
import com.example.progresee.adapters.ExerciseAdapter
import com.example.progresee.adapters.ExerciseClickListener
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentTaskDetailsBinding
import com.example.progresee.viewmodels.TaskDetailsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_task_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class TaskDetailsFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var taskId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTaskDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_details, container, false)

        binding.lifecycleOwner = this


        val arguments = TaskDetailsFragmentArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId
        taskId = arguments.taskId

        val taskDetailsViewModel: TaskDetailsViewModel by viewModel {
            parametersOf(
                appRepository, classroomId, taskId
            )
        }
        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()
        (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.main_menu)
        (activity as? AppCompatActivity)?.progresee_toolbar?.title =
            taskDetailsViewModel.getTask().value?.title
        setItems()
        binding.taskDetailsViewModel = taskDetailsViewModel
        val manager = LinearLayoutManager(context)
        binding.exerciseList.layoutManager = manager
        val adapter = ExerciseAdapter(ExerciseClickListener { exerciseId ->
            taskDetailsViewModel.onTaskClicked(exerciseId)
        })
        binding.exerciseList.adapter = adapter


        taskDetailsViewModel.exercises.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                exercises.text =
                    context!!.getString(R.string.number_of_exercises, 0, adapter.itemCount)
            }
        })

        taskDetailsViewModel.getTask().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.supportActionBar?.title =
                    taskDetailsViewModel.getTask().value!!.title
                binding.task = taskDetailsViewModel.getTask().value
            }
        })




        return binding.root
    }

    private fun setItems() {
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_task_menu_item -> {
                    this.findNavController().navigate(
                        TaskDetailsFragmentDirections.actionTaskDetailsFragmentToCreateTask(
                            classroomId,taskId
                        )
                    )
                }
                R.id.delete_task_menu_item -> {

                }
                R.id.add_exercise_menu_item -> {

                }
                R.id.see_progress_menu_item-> {

                }
            }
            true
        }
    }


}
