package com.example.progresee.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.progresee.R
import com.example.progresee.adapters.UserClickListener
import com.example.progresee.adapters.UsersAdapter
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentClassroomBinding
import com.example.progresee.databinding.FragmentUsersBinding
import com.example.progresee.viewmodels.TaskViewModel
import com.example.progresee.viewmodels.UserViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class UserFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private var classroomId: Long = 0
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUsersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false)

        val arguments = TaskFragmentArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId
        val userViewModel: UserViewModel by viewModel {
            parametersOf(
                appRepository,
                classroomId
            )
        }
        this.userViewModel=userViewModel

        binding.lifecycleOwner = this

        binding.userViewModel=userViewModel

        val manager=LinearLayoutManager(context)
        binding.userList.layoutManager=manager
        val adapter=UsersAdapter(UserClickListener { userId ->
            userViewModel.onUserClicked(userId)
        })
        binding.userList.adapter = adapter

        return  binding.root
    }


}
