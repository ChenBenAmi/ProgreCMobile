package com.example.progresee.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Task

@BindingAdapter("classroomOwner")
fun TextView.setClassroomOwner(item: Classroom?) {
    item?.let {
        text = item.owner
    }
}

@BindingAdapter("numberOfOpenTasks")
fun TextView.setNumberOfOpenTasks(item: Classroom?) {
    item?.let {
        text = item.openTasks.toString()
    }
}


@BindingAdapter("classroomName")
fun TextView.setClassroomName(item: Classroom?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("taskTitle")
fun TextView.setTaskTitle(item: Task?){
    item?.let {
        text = item.title
    }
}

@BindingAdapter("taskDueDate")
fun TextView.setTaskDueDate(item: Task?){
    item?.let{
        text = item.endDate.toString()
    }
}



