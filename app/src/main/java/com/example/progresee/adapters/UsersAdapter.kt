package com.example.progresee.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progresee.beans.Task
import com.example.progresee.beans.User
import com.example.progresee.databinding.ListItemTaskBinding
import com.example.progresee.databinding.ListItemUserBinding


class UsersAdapter(private val clickListener: UserClickListener) : ListAdapter<User,
        UsersAdapter.ViewHolder>(UserDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: UserClickListener, item: User) {
            binding.user = item
            binding.taskClickListener = clickListener
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
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

class UserClickListener(val clickListener: (userId: Long) -> Unit) {
    fun onClick(user: User) = clickListener(user.id)
}
