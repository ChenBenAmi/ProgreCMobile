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
import com.example.progresee.adapters.CheckedListener
import com.example.progresee.adapters.ExerciseAdapter
import com.example.progresee.adapters.ExerciseClickListener
import com.example.progresee.beans.Exercise
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentTaskDetailsBinding
import com.example.progresee.viewmodels.TaskDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_task_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class TaskDetailsFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var taskId: String
    private lateinit var taskDetailsViewModel: TaskDetailsViewModel
    private lateinit var binding: FragmentTaskDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
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
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnClickListener(null)

        taskDetailsViewModel.isAdmin.observe(viewLifecycleOwner, Observer {
            Timber.wtf("isAdmin $it")
            if (it == true) {
                (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.task_details_menu)
                setItemsAdmin()
                createExercise_button.show()
            } else if (it == false) {
                (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.client_menu)
                setItemsClient()
                createExercise_button.hide()
            }
        })
        taskDetailsViewModel.getTask().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.progresee_toolbar?.title =
                    taskDetailsViewModel.getTask().value?.title
            }
        })
        binding.taskDetailsViewModel = taskDetailsViewModel
        val manager = LinearLayoutManager(context)
        binding.exerciseList.layoutManager = manager
        val userEmail = appRepository.getCurrentUserEmail()
        val adapter = ExerciseAdapter(ExerciseClickListener { exercise, context, view ->
            taskDetailsViewModel.onExerciseClicked(exercise, context, view)
        }, CheckedListener {
            taskDetailsViewModel.onExerciseChecked(it)
        }, userEmail!!

        )
        binding.exerciseList.adapter = adapter
        this.taskDetailsViewModel = taskDetailsViewModel
        taskDetailsViewModel.exercises.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.wtf("the list is now changed $it")
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
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
                Timber.wtf(it.toString())
                (activity as? AppCompatActivity)?.supportActionBar?.title =
                    it.title
                Timber.wtf("task is $it")
                binding.task = it
            }
        })

        taskDetailsViewModel.showSnackBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showSnackBar("Exercise added")
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
            if (it != null) {
                this.findNavController().navigate(
                    TaskDetailsFragmentDirections.actionTaskDetailsFragmentToUsersFinishedFragment(
                        classroomId,
                        it
                    )
                )
                taskDetailsViewModel.onDoneNavigatingToUsersFinished()
            }
        })

        taskDetailsViewModel.createExerciseAlert.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                addAlertExercise()
                taskDetailsViewModel.hideCreateExerciseAlert()
            }
        })

        taskDetailsViewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                empty_exercises_list.visibility = View.VISIBLE
                exercise_list.visibility = View.INVISIBLE
            } else {
                empty_exercises_list.visibility = View.GONE
                exercise_list.visibility = View.VISIBLE
            }
        })


        taskDetailsViewModel.navigateToClassroomFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(TaskDetailsFragmentDirections.actionTaskDetailsFragmentToClassroomFragment())
                taskDetailsViewModel.onDoneNavigatingToClassroomFragment()
            }
        })

        taskDetailsViewModel.showSnackBarClassroom.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showSnackBar("Classroom has been deleted :(")
                taskDetailsViewModel.hideSnackBarClassroom()
            }
        })
        taskDetailsViewModel.showSnackBarTask.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showSnackBar("Task has been deleted :(")
                taskDetailsViewModel.hideSnackBarTask()
            }
        })

        taskDetailsViewModel.showSnackBarRefresh.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showSnackBar("Refreshing...")
                taskDetailsViewModel.hideRefreshSnackBar()
            }
        })

        taskDetailsViewModel.showSnackBarUpdatedExercise.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showSnackBar("Exercise Updated")
                taskDetailsViewModel.hideSnackBarUpdatedExercise()
            }
        })
        return binding.root
    }

    private fun setItemsAdmin() {
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
                R.id.see_progress_menu_item -> {
                    //TODO See the entire graph
                }
                R.id.refresh_exercise -> {
                    taskDetailsViewModel.fetchExercisesFromFirebase()
                    taskDetailsViewModel.showSnackBarRefresh()
                }
            }
            true
        }
    }


    private fun setItemsClient() {
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_progress_client -> {
                    if (taskDetailsViewModel.getCheckedList().size > 0) updateExercisesStatus()
                }
                R.id.refresh_client -> {
                    taskDetailsViewModel.fetchExercisesFromFirebase()
                    taskDetailsViewModel.showSnackBarRefresh()
                }
            }

            true
        }
    }

    private fun updateExercisesStatus() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.update_exercises))
        builder.setMessage(
            getString(
                R.string.update_exercises_status,
                taskDetailsViewModel.getCheckedList().size
            )
        )
        builder.setPositiveButton("YES") { dialog, which ->
            taskDetailsViewModel.updateExercisesStatus()
            dialog.cancel()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
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

    private fun addAlertExercise() {
        val builder = AlertDialog.Builder(context!!)
        val exerciseDescription = EditText(context)
        exerciseDescription.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL
        builder.setTitle(R.string.add_exercise)
        builder.setMessage(R.string.enter_exercise_description)
        builder.setView(exerciseDescription)
        builder.setPositiveButton("Confirm") { dialog, which ->
            taskDetailsViewModel.addExercise(exerciseDescription.text.toString())
            dialog.cancel()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()


        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun editAlertExercise(exercise: Exercise) {
        val exerciseDescription = EditText(context)
        exerciseDescription.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL
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
