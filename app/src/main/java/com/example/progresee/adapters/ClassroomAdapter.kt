package com.example.progresee.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.progresee.R
import com.example.progresee.beans.Classroom
import com.example.progresee.databinding.ListItemClassroomBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ClassroomAdapter(private val clickListener: ClassroomClickListener) : ListAdapter<DataItem,
        RecyclerView.ViewHolder>(ClassroomDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Classroom>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.ClassroomItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val classItem = getItem(position) as DataItem.ClassroomItem
                holder.bind(clickListener, classItem.classroom)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.classroom_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ClassroomItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ViewHolder private constructor(private val binding: ListItemClassroomBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: ClassroomClickListener, item: Classroom) {
            this.binding.classroom = item
            this.binding.clickListener = clickListener
            this.binding.executePendingBindings()
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

class ClassroomDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class ClassroomClickListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(classroom: Classroom) = clickListener(classroom.id)
}

sealed class DataItem {
    data class ClassroomItem(val classroom: Classroom): DataItem() {
        override val id = classroom.id
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}

