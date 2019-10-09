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

        val title: String = getString(R.string.progresee)
        binding.lifecycleOwner = this

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
                    .navigate(ClassroomFragmentDirections.actionClassroomFragmentToCreateClassroomFragment())
                classroomViewModel.doneNavigateToCreateClassroomFragment()
            }
        })

        return binding.root

    }
}
