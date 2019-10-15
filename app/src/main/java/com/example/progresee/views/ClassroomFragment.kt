package com.example.progresee.views


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresee.adapters.ClassroomAdapter
import com.example.progresee.adapters.ClassroomClickListener
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentClassroomBinding
import com.example.progresee.viewmodels.ClassroomViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import com.firebase.ui.auth.AuthUI
import com.example.progresee.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_classroom.*
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


        val title: String = getString(R.string.progresee)
        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()
        (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.main_menu)
        (activity as? AppCompatActivity)?.progresee_toolbar?.title = title
        setItems()


        binding.classroomViewModel = classroomViewModel
        val manager = LinearLayoutManager(context)
        binding.classroomList.layoutManager = manager
        adapter = ClassroomAdapter(ClassroomClickListener { classroomId ->
            classroomViewModel.onClassroomClicked(classroomId)
        })
        binding.classroomList.adapter = adapter

        classroomViewModel.getCurrentUser()
        classroomViewModel.classrooms.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
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
                            0
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

        classroomViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true)
                layout_progress_bar.visibility = View.VISIBLE
        })
        return binding.root

    }

    //TODO change when network layer is ready
    private fun setItems() {
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.setting_menu_item -> {
                    Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        "setting",
                        Snackbar.LENGTH_LONG
                    ).show()
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
                this.findNavController()
                    .navigate(ClassroomFragmentDirections.actionClassroomFragmentToHomeFragment())
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "Logged out successfully ",
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }


}
