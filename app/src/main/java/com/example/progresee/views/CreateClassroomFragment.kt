package com.example.progresee.views


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.progresee.R
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentCreateClassroomBinding
import com.example.progresee.viewmodels.BaseViewModel
import com.example.progresee.viewmodels.CreateClassroomViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_classroom.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class CreateClassroomFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var createClassroomViewModel: CreateClassroomViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title =
            context?.getString(R.string.create_classroom_title)

        val binding: FragmentCreateClassroomBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_classroom, container, false)

        binding.lifecycleOwner = this
        val arguments = TaskFragmentArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId
        val createClassroomViewModel: CreateClassroomViewModel by viewModel {
            parametersOf(
                appRepository,
                classroomId
            )
        }
        this.createClassroomViewModel = createClassroomViewModel
        binding.createClassroomViewModel = createClassroomViewModel


        createClassroomViewModel.getClassroom().observe(viewLifecycleOwner, Observer {
            it?.let {
                (activity as? AppCompatActivity)?.progresee_toolbar?.title =
                    context?.getString(R.string.edit_classroom)
                create_classroom_title.text = context?.getString(R.string.edit_classroom)
                editText_classroom_name.setText(it.name)

            }
        })
        createClassroomViewModel.navigateBackToClassroomFragment.observe(
            viewLifecycleOwner,
            Observer {
                if (it == 0L) {
                    this.findNavController()
                        .navigate(CreateClassroomFragmentDirections.actionCreateClassroomFragmentToClassroomFragment())
                    createClassroomViewModel.onDoneNavigating()
                }
            })

        createClassroomViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                hideKeyboard()
                layout_progress_bar.visibility = View.VISIBLE
                save_button.isEnabled = false
                save_button.text = getString(R.string.saving)
            }
        })

        createClassroomViewModel.stringLength.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                showSnackBar(R.string.name_too_long)
            } else if (it == 2) {
                showSnackBar(R.string.name_cant_be_empty)
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
        createClassroomViewModel.snackBarShown()
    }

    //TODO change to util file
    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
