package com.example.progresee.views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.progresee.R
import com.example.progresee.adapters.UserClickListener
import com.example.progresee.adapters.UsersAdapter
import com.example.progresee.data.AppRepository
import com.example.progresee.databinding.FragmentUsersBinding
import com.example.progresee.viewmodels.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class UserFragment : Fragment() {

    private val appRepository: AppRepository by inject()
    private lateinit var classroomId: String
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.progresee_toolbar?.menu?.clear()
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnClickListener(null)
        (activity as? AppCompatActivity)?.progresee_toolbar?.inflateMenu(R.menu.users_in_classroom_menu)
        setItems()

        val binding: FragmentUsersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_users, container, false)

        val arguments = UserFragmentArgs.fromBundle(arguments!!)
        classroomId = arguments.classroomId


        val userViewModel: UserViewModel by viewModel {
            parametersOf(
                appRepository,
                classroomId
            )
        }
        this.userViewModel = userViewModel

        binding.lifecycleOwner = this
        binding.userViewModel = userViewModel

        val manager = LinearLayoutManager(context)
        binding.userList.layoutManager = manager
        val adapter = UsersAdapter(UserClickListener { user, context, view ->
            userViewModel.onUserClicked(user, context, view)
        })
        binding.userList.adapter = adapter

        userViewModel.users.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        userViewModel.transfer.observe(viewLifecycleOwner, Observer {
            it?.let {
                transferAlert(it.first, it.second)
            }
        })
        userViewModel.removeUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                deleteAlert(it.first, it.second)
            }
        })


        userViewModel.transferSuccessful.observe(viewLifecycleOwner, Observer {
            if (it == true)
                showTransferSuccessfulSnackBar()
        })

        userViewModel.removedUserSnackBar.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showRemovedUserSnackBar()
            }
        })

        userViewModel.navigateBackToClassroomFragment.observe(viewLifecycleOwner, Observer {
            if (it == true){
                this.findNavController().navigate(UserFragmentDirections.actionUserFragmentToClassroomFragment())
                userViewModel.doneNavigateToClassroomFragment()
            }
        })

        userViewModel.showSnackBarClassroom.observe(viewLifecycleOwner, Observer {
            if (it == true){
                showSnackBar("Classroom has been deleted :(")
                userViewModel.hideSnackBarClassroomDeleted()
            }
        })

        userViewModel.showSnackBarRefresh.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                showSnackBar("Refreshing...")
                userViewModel.hideRefreshSnackBar()
            }
        })

        return binding.root
    }

    private fun showTransferSuccessfulSnackBar() {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            "Transferred ownership successfully",
            Snackbar.LENGTH_LONG
        ).show()
        userViewModel.hideTransferSuccessful()
        this.findNavController()
            .navigate(UserFragmentDirections.actionUserFragmentToClassroomFragment())
    }

    private fun showRemovedUserSnackBar() {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            "Removed user successfully",
            Snackbar.LENGTH_LONG
        ).show()
        userViewModel.hideRemoveUserSnackBar()
    }


    private fun deleteAlert(userName: String, userUid: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.remove_user))
        builder.setMessage("Are you sure you want to remove $userName from this classroom?")
        builder.setPositiveButton("YES") { dialog, which ->
            userViewModel.removeUser(userUid)
            userViewModel.hideRemoveUserDialog()
        }
        builder.setNegativeButton("No") { dialog, which ->
            userViewModel.hideRemoveUserDialog()

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun transferAlert(userName: String, userUid: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.transfer_ownership))
        builder.setMessage("Are you sure you want to transfer this classroom ownership to $userName? \nWarning: this cannot be undone")
        builder.setPositiveButton("Confirm") { dialog, which ->
            userViewModel.transferClassroom(userUid)
            userViewModel.hideTransferDialog()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            userViewModel.hideTransferDialog()
        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }


    private fun setItems() {
        (activity as? AppCompatActivity)?.progresee_toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh_users_in_classroom -> {
                    userViewModel.loadUsers()
                    userViewModel.showSnackBarRefresh()
                }
            }
            true
        }
    }

    private fun showSnackBar(message:String) {
        Snackbar.make(
            activity!!.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}
