package com.example.progresee.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progresee.beans.Exercise
import com.example.progresee.databinding.ListItemExerciseBinding
import com.example.progresee.views.UserFragment


class ExerciseAdapter(
    private val clickListener: ExerciseClickListener,
    private val checkedListener: CheckedListener,private val userEmail:String
) : ListAdapter<Exercise,
        ExerciseAdapter.ViewHolder>(ExerciseDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, checkedListener, item,userEmail)
        if (UserFragment.owner) {
            holder.binding.threeDots.visibility = View.VISIBLE
        } else {
            holder.binding.threeDots.visibility = View.GONE
            holder.binding.exerciseStatus.visibility = View.VISIBLE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            clickListener: ExerciseClickListener,
            checkedListener: CheckedListener,
            item: Exercise,userEmail: String
        ) {
            binding.exercise = item
            binding.exerciseClickListener = clickListener
            binding.checkedListener = checkedListener
            binding.userEmail=userEmail
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
    fun onClick(exercise: Exercise, context: Context, view: View) =
        clickListener(exercise, context, view)
}

class CheckedListener(val clickListener: (exercise: Exercise) -> Unit) {
    fun onClick(exercise: Exercise) = clickListener(exercise)
}


