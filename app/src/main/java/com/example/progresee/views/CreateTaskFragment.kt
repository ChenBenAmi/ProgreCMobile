package com.example.progresee.views


import android.app.Activity
import android.app.DatePickerDialog
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
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentCreateTaskBinding
import com.example.progresee.viewmodels.CreateTaskViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_classroom.*
import kotlinx.android.synthetic.main.fragment_create_task.*
import kotlinx.android.synthetic.main.fragment_task.layout_progress_bar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


class CreateTaskFragment : Fragment() {

    private val appRepository: AppRepository by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title =
            context?.getString(R.string.create_task)

        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()

        val binding: FragmentCreateTaskBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_task, container, false)

        val args = CreateTaskFragmentArgs.fromBundle(arguments!!)
        val taskId = args.taskId
        val classroomId = args.classroomId
        val createTaskViewModel: CreateTaskViewModel = getViewModel {
            parametersOf(
                appRepository,
                classroomId, taskId
            )
        }

        binding.lifecycleOwner = this



        binding.createClassroomViewModel = createTaskViewModel


        createTaskViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                hideKeyboard()
                layout_progress_bar.visibility = View.VISIBLE
                save_button.isEnabled = false
                save_button.text = getString(R.string.saving)
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
                Timber.wtf("hey")
                datePicker()
                Timber.wtf("hey1")
                createTaskViewModel.onPickDateFinished()
                Timber.wtf("hey2")
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
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                current_end_date.text = """$dayOfMonth $monthOfYear, $year"""
            },
            year,
            month,
            day
        )

        dpd.show()
    }


}
