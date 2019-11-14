package com.app.progrec.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.progrec.beans.User
import android.content.Context
import android.view.View
import com.app.progrec.databinding.ListItemUserBinding
import com.app.progrec.views.ClassroomFragment


class UsersAdapter(private val clickListener: UserClickListener) : ListAdapter<User,
        UsersAdapter.ViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
        if (ClassroomFragment.owner) {
            holder.binding.threeDots.visibility=View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: UserClickListener, item: User) {
            binding.user = item
            binding.userClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemUserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

class UserClickListener(val clickListener: (user: User,context:Context,view:View) -> Unit) {
    fun onClick(user: User,context: Context,view: View) = clickListener(user,context,view)
}
