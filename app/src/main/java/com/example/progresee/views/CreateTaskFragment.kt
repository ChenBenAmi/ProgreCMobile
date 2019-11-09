package com.example.progresee.views


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.transition.Visibility
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.SharedElementCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentCreateTaskBinding
import com.example.progresee.viewmodels.CreateTaskViewModel
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_task.*
import kotlinx.android.synthetic.main.fragment_task.layout_progress_bar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.*


class CreateTaskFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)
    private lateinit var classroomId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title =
            context?.getString(R.string.create_task)
        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()

        val binding: FragmentCreateTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_task, container, false)
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnClickListener(null)


        val args = CreateTaskFragmentArgs.fromBundle(arguments!!)
        val taskId = args.taskId
        classroomId = args.classroomId
        val createTaskViewModel: CreateTaskViewModel = getViewModel {
            parametersOf(
                appRepository,
                classroomId, taskId
            )
        }


        binding.lifecycleOwner = this



        binding.createTaskViewModel = createTaskViewModel


        createTaskViewModel.getTask().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.progresee_toolbar?.title =
                    context?.getString(R.string.edit_task)
                create_task_title.text = context?.getString(R.string.edit_task)
                edit_text_task_title.setText(it.title)
                edit_text_task_description.setText(it.description)
                if (it.referenceLink != null) {
                    add_link_task.setText(it.referenceLink)
                }
                current_end_date.text = it.endDate
            }
        })

        createTaskViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                hideKeyboard()
                layout_progress_bar.visibility = View.VISIBLE
                save_task.isEnabled = false
                save_task.text = getString(R.string.saving)
            }
        })

        createTaskViewModel.navigateBackToTaskFragment.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    this.findNavController()
                        .navigate(
                            CreateTaskFragmentDirections.actionCreateTaskToTaskFragment(
                                classroomId
                            )
                        )
                    createTaskViewModel.onDoneNavigating()
                }
            })

        createTaskViewModel.stringLength.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                showSnackBar(R.string.name_too_long)
                createTaskViewModel.snackBarShown()
            } else if (it == 2) {
                showSnackBar(R.string.name_cant_be_empty)
                createTaskViewModel.snackBarShown()
            }
        })

        createTaskViewModel.descriptionStringLength.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                showSnackBar(R.string.description_too_long)
                createTaskViewModel.snackBarShown()
            } else if (it == 2) {
                showSnackBar(R.string.description_cant_be_empty)
                createTaskViewModel.snackBarShown()
            }
        })

        createTaskViewModel.pickDate.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                datePicker()
                createTaskViewModel.onPickDateFinished()
            }
        })

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(
                    CreateTaskFragmentDirections.actionCreateTaskToTaskFragment(
                        classroomId
                    )
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)


        return binding.root
    }



    private fun showSnackBar(id: Int) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            getString(id),
            Snackbar.LENGTH_LONG
        ).show()

    }


    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun datePicker() {
        hideKeyboard()


        val dpd = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                current_end_date.text =
                    getString(R.string.current_date, dayOfMonth, monthOfYear + 1, year)
                Timber.wtf("$dayOfMonth + ${monthOfYear + 1} + $year")
            },
            year,
            month,
            day
        )
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
        current_end_date.visibility = View.VISIBLE
    }

}
