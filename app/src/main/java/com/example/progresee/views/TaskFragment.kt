package com.example.progresee.views


import android.content.DialogInterface
import android.os.Bundle
import android.view.*
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
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class TaskFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private var classroomId: Long = 0
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        val arguments = TaskFragmentArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId
        val taskViewModel: TaskViewModel by viewModel {
            parametersOf(
                appRepository,
                classroomId
            )
        }
        this.taskViewModel = taskViewModel
        binding.taskViewModel = taskViewModel

        val manager = LinearLayoutManager(context)
        binding.taskList.layoutManager = manager
        val adapter = TaskAdapter(TaskClickListener { taskId ->
            taskViewModel.onTaskClicked(taskId)
        })
        binding.taskList.adapter = adapter

//        taskViewModel.insertDummyData()

        taskViewModel.getClassroom().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.supportActionBar?.title =
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

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.classroom_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //TODO change when network layer is ready
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_menu_item -> {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "edit",
                    Snackbar.LENGTH_LONG
                ).show()
                this.findNavController().navigate(
                    TaskFragmentDirections.actionTaskFragmentToCreateClassroomFragment(classroomId)
                )
                return true
            }
            R.id.delete_menu_item -> {
                deleteAlert()
                return true
            }
            R.id.info_menu_item -> {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "info",
                    Snackbar.LENGTH_LONG
                ).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
}
