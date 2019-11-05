package com.example.progresee.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progresee.beans.FinishedUser
import com.example.progresee.databinding.ListItemFinishedUserBinding


class UserFinishedAdapter() : ListAdapter<FinishedUser,
        UserFinishedAdapter.ViewHolder>(UserFinishedDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder private constructor(val binding: ListItemFinishedUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FinishedUser) {
            binding.userFinished = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFinishedUserBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}

class UserFinishedDiffCallback : DiffUtil.ItemCallback<FinishedUser>() {
    override fun areItemsTheSame(oldItem: FinishedUser, newItem: FinishedUser): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: FinishedUser, newItem: FinishedUser): Boolean {
        return oldItem == newItem
    }
}


