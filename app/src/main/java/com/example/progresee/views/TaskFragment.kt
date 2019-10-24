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
import kotlinx.android.synthetic.main.fragment_task.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class TaskFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var emailText: EditText
    private var owner = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)

        binding.lifecycleOwner = this

        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()
        setItems()
        (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.classroom_menu)
        emailText = EditText(context)
        emailText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

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
            if (it == null) layout_progress_bar.visibility = View.GONE
        })

        taskViewModel.getClassroom().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.progresee_toolbar?.title =
                    taskViewModel.getClassroom().value!!.name
                taskViewModel.checkClassroomOwnerShip(it)
            }
        })

        taskViewModel.checkOwnerShip.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.getItem(0)?.isVisible =
                    true
                (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.getItem(1)?.isVisible =
                    true
                (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.getItem(2)?.isVisible =
                    true
                owner = true
                taskViewModel.checkedClassroomOwnerShip()
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
                    this.findNavController()
                        .navigate(
                            TaskFragmentDirections.actionTaskFragmentToTaskDetailsFragment(
                                taskId
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
            showUserAdded()
            taskViewModel.snackBarShown()
        })



        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnClickListener {
            this.findNavController()
                .navigate(
                    TaskFragmentDirections.actionTaskFragmentToUserFragment(
                        classroomId,
                        owner
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


        return binding.root

    }


    //TODO change when network layer is ready
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
            }
            true
        }
    }

    private fun deleteAlert() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.delete_are_you_sure)
        builder.setPositiveButton("YES") { dialog, which ->
            taskViewModel.deleteClassRoom()
        }
        builder.setNegativeButton("No") { dialog, which ->

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
        builder.setTitle(R.string.add_person)
        builder.setMessage(R.string.enter_user_email)
        builder.setView(emailText)
        builder.setPositiveButton("Confirm") { dialog, which ->
            taskViewModel.addToClassRoom(emailText.text.toString())
        }
        builder.setNegativeButton("Cancel") { dialog, which ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    companion object {
        const val ARG_TEMPLATE_CODE = "someString"
    }


}
