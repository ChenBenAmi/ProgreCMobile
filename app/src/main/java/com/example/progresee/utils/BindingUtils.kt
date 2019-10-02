package com.example.progresee.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.progresee.beans.Classroom

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