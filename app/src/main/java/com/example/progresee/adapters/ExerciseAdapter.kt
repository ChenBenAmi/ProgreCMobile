package com.example.progresee.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.User
import com.example.progresee.databinding.ListItemExerciseBinding


class ExerciseAdapter(private val clickListener: ExerciseClickListener) : ListAdapter<Exercise,
        ExerciseAdapter.ViewHolder>(ExerciseDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: ExerciseClickListener, item: Exercise) {
            binding.exercise = item
            binding.exerciseClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemExerciseBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem == newItem
    }
}

class ExerciseClickListener(val clickListener: (exercise: Exercise, context: Context, view: View) -> Unit) {
    fun onClick(exercise: Exercise, context: Context, view: View) = clickListener(exercise,context,view)
}
