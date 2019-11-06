package com.example.progresee.views

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.progresee.R
import com.example.progresee.adapters.ExerciseAdapter
import com.example.progresee.adapters.ExerciseClickListener
import com.example.progresee.adapters.UserFinishedAdapter
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentTaskDetailsBinding
import com.example.progresee.databinding.UsersFinishedFragmentBinding
import com.example.progresee.viewmodels.TaskDetailsViewModel
import com.example.progresee.viewmodels.UsersFinishedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class UsersFinishedFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var exerciseId: String
    private lateinit var viewModel: UsersFinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.completed_list_title)
        val binding: UsersFinishedFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.users_finished_fragment, container, false)

        binding.lifecycleOwner = this


        val arguments = UsersFinishedFragmentArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId
        exerciseId = arguments.exerciseId

        Timber.wtf("classroomId is $classroomId exerciseId is $exerciseId")
        val viewModel: UsersFinishedViewModel by viewModel {
            parametersOf(
                appRepository, classroomId, exerciseId
            )
        }
        this.viewModel = viewModel
        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()


        binding.usersFinishedViewModel = viewModel
        val manager = LinearLayoutManager(context)
        binding.usersFinishedList.layoutManager = manager
        val adapter = UserFinishedAdapter()

        binding.usersFinishedList.adapter = adapter

        viewModel.usersFinished.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.wtf("hey i have some users for you")
                Timber.wtf(it.toString())
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}
