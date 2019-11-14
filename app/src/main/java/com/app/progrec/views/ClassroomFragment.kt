package com.app.progrec.views


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
import com.app.progrec.R
import com.app.progrec.adapters.ClassroomAdapter
import com.app.progrec.adapters.ClassroomClickListener
import com.app.progrec.data.AppRepository
import com.app.progrec.databinding.FragmentClassroomBinding
import com.app.progrec.viewmodels.ClassroomViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_classroom.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ClassroomFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private val classroomViewModel: ClassroomViewModel by viewModel { parametersOf(appRepository) }
    private lateinit var adapter: ClassroomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentClassroomBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_classroom, container, false)

        binding.lifecycleOwner = this

        classroomViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true)
                layout_progress_bar_classroom.visibility = View.VISIBLE
            createClassroom_button.isEnabled=false
            if (it == false) {
                layout_progress_bar_classroom.visibility = View.GONE
                createClassroom_button.isEnabled=true
            }
        })

        val title: String = getString(R.string.progrec)
        (activity as? AppCompatActivity)?.progresee_toolbar?.title = title
        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()
        (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.main_menu)
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnClickListener(null)
        setItems()

        classroomViewModel.isAdmin.observe(viewLifecycleOwner, Observer {
            if (it == false) {
                createClassroom_button.hide()
            } else if (it == true) {
                owner = true
            }
        })



        binding.classroomViewModel = classroomViewModel
        val manager = LinearLayoutManager(context)
        binding.classroomList.layoutManager = manager
        adapter = ClassroomAdapter(ClassroomClickListener { classroomId ->
            classroomViewModel.onClassroomClicked(classroomId)
        })
        binding.classroomList.adapter = adapter


        classroomViewModel.classrooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                classroom_list.visibility = View.VISIBLE
            }

        })


        classroomViewModel.navigateToTaskFragment.observe(
            viewLifecycleOwner,
            Observer { classroom ->
                classroom?.let {
                    this.findNavController()
                        .navigate(
                            ClassroomFragmentDirections.actionClassroomFragmentToTaskFragment(
                                classroom
                            )
                        )
                    classroomViewModel.doneNavigateToTaskFragment()
                }
            })

        classroomViewModel.navigateToCreateClassroomFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(
                        ClassroomFragmentDirections.actionClassroomFragmentToCreateClassroomFragment(
                            "none"
                        )
                    )
                classroomViewModel.doneNavigateToCreateClassroomFragment()
            }
        })

        classroomViewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                empty_classroom_view.visibility = View.VISIBLE
                classroom_list.visibility = View.GONE
            } else {
                empty_classroom_view.visibility = View.GONE
                classroom_list.visibility = View.VISIBLE
            }
        })

        classroomViewModel.showSnackBarRefresh.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                R.string.refreshing_string.showSnackBar()
                classroomViewModel.hideRefreshSnackBar()
            }
        })
        appRepository.setUserEmail()

        classroomViewModel.showSnackBarHttpError.observe(viewLifecycleOwner, Observer {
            if (it==1) {
                R.string.not_part_of_classroom_error.showSnackBar()
                classroomViewModel.hideHttpErrorSnackBar()
            } else if (it==2) {
                R.string.network_error.showSnackBar()
                classroomViewModel.hideHttpErrorSnackBar()
            }
        })

        return binding.root

    }


    private fun Int.showSnackBar() {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            getString(this),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setItems() {
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh_classroom -> {
                  classroomViewModel.fetchClassrooms()
                    classroomViewModel.showSnackBarRefresh()
                }
                R.id.logout_menu_item -> {
                    logout()
                }
            }
            true
        }
    }


    private fun logout() {
        AuthUI.getInstance().signOut(context!!.applicationContext)
            .addOnCompleteListener {
                appRepository.removeToken()
                this.findNavController()
                    .navigate(ClassroomFragmentDirections.actionClassroomFragmentToHomeFragment())
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "Logged out successfully ",
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }




    companion object {
        var owner = false
    }


}
