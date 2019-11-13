package com.example.progresee.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresee.R
import com.example.progresee.adapters.UserFinishedAdapter
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FinishedUsersFragmentBinding
import com.example.progresee.viewmodels.UsersFinishedViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.finished_users_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class FinishedUsersFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var exerciseId: String
    private lateinit var viewModel: UsersFinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FinishedUsersFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.finished_users_fragment, container, false)

        val title: String = getString(R.string.finished_users)
        (activity as? AppCompatActivity)?.progresee_toolbar?.title = title
        (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.users_finished_menu)
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnClickListener(null)
        setItems()

        binding.lifecycleOwner = this


        val arguments = FinishedUsersFragmentArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId
        exerciseId = arguments.exerciseId

        Timber.wtf("classroomId is $classroomId exerciseId is $exerciseId")
        val finishedUsersViewModel: UsersFinishedViewModel by viewModel {
            parametersOf(
                appRepository, classroomId, exerciseId
            )
        }
        this.viewModel = finishedUsersViewModel
        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()


        binding.usersFinishedViewModel = finishedUsersViewModel
        val manager = LinearLayoutManager(context)
        binding.usersFinishedList.layoutManager = manager
        val adapter = UserFinishedAdapter()

        binding.usersFinishedList.adapter = adapter

        finishedUsersViewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.completed_list_title)
                empty_finished_users_list.visibility = View.VISIBLE
                users_finished_list.visibility = View.INVISIBLE
            } else {
                (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.completed_list_title)
                empty_finished_users_list.visibility = View.GONE
                users_finished_list.visibility = View.VISIBLE
            }
        })
        finishedUsersViewModel.usersFinished.observe(viewLifecycleOwner, Observer {
            it?.let {
                Timber.wtf(it.toString())
                adapter.submitList(it)
            }
        })

        finishedUsersViewModel.showSnackBarRefresh.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                R.string.refreshing_string.showSnackBar()
                finishedUsersViewModel.hideRefreshSnackBar()
            }
        })
        finishedUsersViewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            if (it == true)
                layout_progress_bar_finished_users.visibility = View.VISIBLE
            if (it == false) {
                layout_progress_bar_finished_users.visibility = View.GONE
            }
        })

        finishedUsersViewModel.showSnackBarHttpError.observe(viewLifecycleOwner, Observer {
            if (it==1) {
                R.string.no_users_finished.showSnackBar()
                finishedUsersViewModel.hideHttpErrorSnackBar()
            } else if (it==2) {
                R.string.network_error.showSnackBar()
                finishedUsersViewModel.hideHttpErrorSnackBar()
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
                R.id.refresh_users_finished -> {
                    viewModel.getUsersFinished()
                    viewModel.showSnackBarRefresh()
                }
            }
            true
        }
    }
}
