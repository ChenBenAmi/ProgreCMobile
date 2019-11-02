package com.example.progresee.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progresee.beans.User
import android.content.Context
import android.view.View
import com.example.progresee.beans.UserFinished
import com.example.progresee.databinding.ListItemFinishedUserBinding
import com.example.progresee.databinding.ListItemUserBinding
import com.example.progresee.views.UserFragment


class UserFinishedAdapter() : ListAdapter<UserFinished,
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

        fun bind(item: UserFinished) {
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

class UserFinishedDiffCallback : DiffUtil.ItemCallback<UserFinished>() {
    override fun areItemsTheSame(oldItem: UserFinished, newItem: UserFinished): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: UserFinished, newItem: UserFinished): Boolean {
        return oldItem == newItem
    }
}


