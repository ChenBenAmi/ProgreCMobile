package com.example.progresee.views


import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentCreateTaskBinding
import com.example.progresee.viewmodels.CreateTaskViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_classroom.*
import kotlinx.android.synthetic.main.fragment_task.layout_progress_bar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CreateTask : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private var taskId: String? = null
    private lateinit var createTaskViewModel: CreateTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title =
            context?.getString(R.string.create_task)

        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()

        val binding: FragmentCreateTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_task, container, false)



        binding.lifecycleOwner = this

        val arguments = CreateTaskArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId
        taskId = arguments.taskId
        val createTaskViewModel: CreateTaskViewModel by viewModel {
            parametersOf(
                appRepository,
                classroomId, taskId
            )
        }
        this.createTaskViewModel = createTaskViewModel
        binding.createClassroomViewModel = this.createTaskViewModel


        this.createTaskViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                hideKeyboard()
                layout_progress_bar.visibility = View.VISIBLE
                save_button.isEnabled = false
                save_button.text = getString(R.string.saving)
            }
        })

        this.createTaskViewModel.navigateBackToTaskFragment.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    this.findNavController()
                        .navigate(CreateTaskDirections.actionCreateTaskToTaskFragment(classroomId))
                    createTaskViewModel.onDoneNavigating()
                }
            })

        this.createTaskViewModel.stringLength.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                showSnackBar(R.string.name_too_long)
            } else if (it == 2) {
                showSnackBar(R.string.name_cant_be_empty)
            }
        })

        this.createTaskViewModel.descriptionStringLength.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                showSnackBar(R.string.description_too_long)
            } else if (it == 2) {
                showSnackBar(R.string.description_cant_be_empty)
            }
        })
        return binding.root
    }

    private fun showSnackBar(id: Int) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            getString(id),
            Snackbar.LENGTH_LONG
        ).show()
        createTaskViewModel.snackBarShown()
    }


    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}
