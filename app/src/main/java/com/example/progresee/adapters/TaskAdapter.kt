package com.example.progresee.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progresee.beans.Task
import com.example.progresee.databinding.ListItemTaskBinding


class TaskAdapter(private val clickListener: TaskClickListener) : ListAdapter<Task,
        TaskAdapter.ViewHolder>(TaskDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: TaskClickListener, item: Task) {
            binding.task = item
            binding.taskClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}

class TaskClickListener(val clickListener: (TaskId: Long) -> Unit) {
    fun onClick(Task: Task) = clickListener(Task.id)
}
