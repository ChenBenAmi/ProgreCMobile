package com.example.progresee.views


import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import android.content.Intent
import androidx.annotation.NonNull
import com.firebase.ui.auth.AuthUI
import android.content.Context
import com.example.progresee.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


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

        setHasOptionsMenu(true)

        binding.classroomViewModel = classroomViewModel
        val manager = LinearLayoutManager(context)
        binding.classroomList.layoutManager = manager
        adapter = ClassroomAdapter(ClassroomClickListener { classroomId ->
            classroomViewModel.onClassroomClicked(classroomId)
        })


        binding.classroomList.adapter = adapter
        val titleCheck: Boolean? =
            (activity as? AppCompatActivity)?.supportActionBar?.title?.equals(title)
        if (titleCheck == false) {
            (activity as? AppCompatActivity)?.supportActionBar?.title = title
        }
//        classroomViewModel.insertDummyData()

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
                    .navigate(ClassroomFragmentDirections.actionClassroomFragmentToCreateClassroomFragment(0))
                classroomViewModel.doneNavigateToCreateClassroomFragment()
            }
        })

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //TODO change when network layer is ready
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_menu_item -> {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "setting",
                    Snackbar.LENGTH_LONG
                ).show()
                return true
            }
            R.id.logout_menu_item -> {
                logout()
                return true
            }
            android.R.id.home-> {
                return NavigationUI.onNavDestinationSelected(item,view!!.findNavController())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        AuthUI.getInstance().signOut(context!!.applicationContext)
            .addOnCompleteListener {
                this.findNavController().navigate(ClassroomFragmentDirections.actionClassroomFragmentToHomeFragment())
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    "Logged out successfully",
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }

}
