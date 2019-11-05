package com.example.progresee.views


import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresee.R
import com.example.progresee.adapters.ExerciseAdapter
import com.example.progresee.adapters.ExerciseClickListener
import com.example.progresee.beans.Exercise
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentTaskDetailsBinding
import com.example.progresee.viewmodels.TaskDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class TaskDetailsFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var taskId: String
    private lateinit var taskDetailsViewModel: TaskDetailsViewModel
    private lateinit var exerciseDescription: EditText
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

        Timber.wtf("classroomId is $classroomId taskId is $taskId")
        val taskDetailsViewModel: TaskDetailsViewModel by viewModel {
            parametersOf(
                appRepository, classroomId, taskId
            )
        }
        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()
        (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.task_details_menu)
        (activity as? AppCompatActivity)?.progresee_toolbar?.title =
            taskDetailsViewModel.getTask().value?.title
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnClickListener(null)
        setItems()
        binding.taskDetailsViewModel = taskDetailsViewModel
        val manager = LinearLayoutManager(context)
        binding.exerciseList.layoutManager = manager
        val adapter = ExerciseAdapter(ExerciseClickListener {  exercise, context, view ->
            taskDetailsViewModel.onExerciseClicked(exercise, context, view)
        })
        binding.exerciseList.adapter = adapter

        exerciseDescription = EditText(context)
        exerciseDescription.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL
        this.taskDetailsViewModel = taskDetailsViewModel
        taskDetailsViewModel.getExercises().observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.wtf(it.toString())
                adapter.submitList(it)
                context!!.getString(R.string.number_of_exercises, adapter.itemCount)
            }
        })

        taskDetailsViewModel.navigateToTaskFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    TaskDetailsFragmentDirections.actionTaskDetailsFragmentToTaskFragment(
                        classroomId
                    )
                )
            }
        })
        taskDetailsViewModel.getTask().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.supportActionBar?.title =
                    it.title
                Timber.wtf("task is $it")
                binding.task = it
            }
        })

        taskDetailsViewModel.showSnackBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showExerciseAdded()
                taskDetailsViewModel.snackBarShown()
            }
        })

        taskDetailsViewModel.editExercise.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                editAlertExercise(it)

            }
        })

        taskDetailsViewModel.removeExercise.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                deleteAlertExercise(it)
            }
        })

        taskDetailsViewModel.navigateToUsersFinished.observe(viewLifecycleOwner, Observer {
            if(it != null){
                this.findNavController().navigate(TaskDetailsFragmentDirections.actionTaskDetailsFragmentToUsersFinishedFragment(classroomId, it))
                taskDetailsViewModel.onDoneNavigatingToUsersFinished()
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
                            classroomId, taskId
                        )
                    )
                }
                R.id.delete_task_menu_item -> {
                    deleteAlertTask()
                }
                R.id.add_exercise_menu_item -> {
                    addAlertTask()
                }
                R.id.see_progress_menu_item -> {
//TODO asdasdf
                }
            }
            true
        }
    }


    private fun deleteAlertTask() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.delete_are_you_sure_task)
        builder.setPositiveButton("YES") { dialog, which ->
            taskDetailsViewModel.deleteTask()
            dialog.cancel()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun addAlertTask() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(R.string.add_exercise)
        builder.setMessage(R.string.enter_exercise_description)
        builder.setView(exerciseDescription)
        builder.setPositiveButton("Confirm") { dialog, which ->
            taskDetailsViewModel.addExercise(exerciseDescription.text.toString())
            dialog.cancel()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun showExerciseAdded() {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            "Exercise added",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun editAlertExercise(exercise: Exercise) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.edit_exercise))
        exerciseDescription.setText(exercise.exerciseTitle)
        builder.setView(exerciseDescription)

        builder.setPositiveButton("Confirm") { dialog, which ->
            taskDetailsViewModel.updateExercise(exercise, exerciseDescription.text.toString())
            taskDetailsViewModel.hideEditExerciseDialog()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            taskDetailsViewModel.hideEditExerciseDialog()
        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun deleteAlertExercise(uid: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.delete_exercise))
        builder.setMessage("Are you sure you want to remove this exercise?!")
        builder.setPositiveButton("YES") { dialog, which ->
            taskDetailsViewModel.deleteExercise(uid)
            taskDetailsViewModel.hideRemoveExerciseDialog()
        }
        builder.setNegativeButton("No") { dialog, which ->
            taskDetailsViewModel.hideRemoveExerciseDialog()

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }


}
