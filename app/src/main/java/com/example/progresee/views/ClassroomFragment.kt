package com.example.progresee.views


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
import com.example.progresee.R
import com.example.progresee.adapters.ClassroomAdapter
import com.example.progresee.adapters.ClassroomClickListener
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentClassroomBinding
import com.example.progresee.viewmodels.ClassroomViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_classroom.*
import kotlinx.android.synthetic.main.fragment_create_classroom.layout_progress_bar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


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
                layout_progress_bar.visibility = View.VISIBLE
            if (it == false) {
                classroom_list.visibility = View.VISIBLE
            }
        })

        val title: String = getString(R.string.progresee)
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

        classroomViewModel.user.observe(viewLifecycleOwner, Observer {
            it?.let {
                layout_progress_bar.visibility = View.GONE
                Timber.wtf(it.toString())
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
                showSnackBar("Refreshing...")
                classroomViewModel.hideRefreshSnackBar()
            }
        })
        appRepository.setUserEmail()

        return binding.root

    }


    private fun showSnackBar(message: String) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            message,
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
