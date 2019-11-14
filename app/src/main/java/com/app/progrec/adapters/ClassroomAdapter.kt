package com.app.progrec.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.progrec.beans.Classroom
import com.app.progrec.databinding.ListItemClassroomBinding

class ClassroomAdapter(private val clickListener: ClassroomClickListener) : ListAdapter<Classroom,
        ClassroomAdapter.ViewHolder>(ClassroomDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


    class ViewHolder constructor(val binding: ListItemClassroomBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(clickListener: ClassroomClickListener, item: Classroom) {
            binding.classroom = item
            binding.classroomClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemClassroomBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class ClassroomDiffCallback : DiffUtil.ItemCallback<Classroom>() {
    override fun areItemsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: Classroom, newItem: Classroom): Boolean {
        return oldItem == newItem
    }
}

class ClassroomClickListener(val clickListener: (classroomId: String) -> Unit) {
    fun onClick(classroom: Classroom) = clickListener(classroom.uid)
}