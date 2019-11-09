package com.example.progresee.views


import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresee.R
import com.example.progresee.adapters.TaskAdapter
import com.example.progresee.adapters.TaskClickListener
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentTaskBinding
import com.example.progresee.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_classroom.*
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.android.synthetic.main.fragment_task.layout_progress_bar
import kotlinx.android.synthetic.main.fragment_task_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class TaskFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var taskViewModel: TaskViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)

        binding.lifecycleOwner = this

        val arguments = TaskFragmentArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId
        val taskViewModel: TaskViewModel by viewModel {
            parametersOf(
                appRepository,
                classroomId
            )
        }
        this.taskViewModel = taskViewModel
        binding.taskViewModel = this.taskViewModel

        taskViewModel.isAdmin.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()
                (activity as? AppCompatActivity)?.progresee_toolbar!!.inflateMenu(R.menu.classroom_menu)
                setItems()
                create_task_button.show()
            } else if (it == false) {
                create_task_button.hide()
            }
        })

        val manager = LinearLayoutManager(context)
        binding.taskList.layoutManager = manager
        val adapter = TaskAdapter(TaskClickListener { taskId ->
            taskViewModel.onTaskClicked(taskId)
        })
        binding.taskList.adapter = adapter

        taskViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                layout_progress_bar.visibility = View.VISIBLE
                create_task_button.isEnabled = false
            }
            if (it == null) {
                layout_progress_bar.visibility = View.GONE
                create_task_button.isEnabled = true
            }
        })

        taskViewModel.getClassroom().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.progresee_toolbar?.title =
                    taskViewModel.getClassroom().value!!.name

            }
        })


        taskViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        taskViewModel.navigateToTaskDetailsFragment.observe(
            viewLifecycleOwner,
            Observer { taskId ->
                taskId?.let {
                    Timber.wtf("classroomId is $classroomId taskId is $taskId")
                    this.findNavController()
                        .navigate(
                            TaskFragmentDirections.actionTaskFragmentToTaskDetailsFragment(
                                taskId,
                                classroomId
                            )
                        )
                    taskViewModel.doneNavigateToTaskDetailsFragment()
                }
            })
        taskViewModel.navigateBackToClassroomFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(TaskFragmentDirections.actionTaskFragmentToClassroomFragment())
                taskViewModel.doneNavigateToClassroomFragment()
            }
        })

        taskViewModel.showSnackBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showUserAdded()
                taskViewModel.snackBarShown()
            }
        })



        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnClickListener {
            this.findNavController()
                .navigate(
                    TaskFragmentDirections.actionTaskFragmentToUserFragment(
                        classroomId
                    )
                )
        }

        taskViewModel.navigateToCreateTask.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                    TaskFragmentDirections.actionTaskFragmentToCreateTask(
                        classroomId,
                        null
                    )
                )
            }
        })

        taskViewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            if (it == true){
                empty_tasks_view.visibility = View.VISIBLE
                task_list.visibility = View.GONE
            } else {
                empty_tasks_view.visibility = View.GONE
                task_list.visibility = View.VISIBLE
            }
        })
        return binding.root

    }


    private fun setItems() {
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_menu_item -> {
                    this.findNavController().navigate(
                        TaskFragmentDirections.actionTaskFragmentToCreateClassroomFragment(
                            classroomId
                        )
                    )
                }
                R.id.delete_menu_item -> {
                    deleteAlert()
                }
                R.id.add_user_menu_item -> {
                    addAlert()
                }
                R.id.refresh_task -> {
                    taskViewModel.fetchTasksFromFirebase()
                }
            }
            true
        }
    }

    private fun deleteAlert() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.delete_are_you_sure_classroom)
        builder.setPositiveButton("YES") { dialog, which ->
            taskViewModel.deleteClassRoom()
            dialog.cancel()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()


        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun showUserAdded() {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            "User added",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun addAlert() {
        val builder = AlertDialog.Builder(context!!)
        val emailText = EditText(context)
        emailText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        builder.setTitle(R.string.add_person)
        builder.setMessage(R.string.enter_user_email)
        builder.setView(emailText)
        builder.setPositiveButton("Confirm") { dialog, which ->
            taskViewModel.addToClassRoom(emailText.text.toString())
            dialog.cancel()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }


}
