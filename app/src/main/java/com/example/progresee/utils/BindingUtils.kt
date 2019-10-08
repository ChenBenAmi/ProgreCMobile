package com.example.progresee.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.progresee.R
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import timber.log.Timber

@BindingAdapter("classroomOwner")
fun TextView.setClassroomOwner(item: Classroom?) {
    item?.let {
        text = item.owner
    }
}

@BindingAdapter("numberOfOpenTasks")
fun TextView.setNumberOfOpenTasks(item: Classroom?) {
    item?.let {
        text = context.getString(R.string.number_of_tasks, item.openTasks)
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
        text = context.getString(R.string.due_by, item.endDate.toString())
    }
}

@BindingAdapter("taskDescription")
fun TextView.setTaskDescription(item: Task?){
    item?.let{
        text = item.description
    }
}

@BindingAdapter("taskImage")
fun ImageView.setTaskImage(item: Task?){
    Glide.with(context)
        .load(item?.imageURL)
        .into(this)
}

@BindingAdapter("exerciseText")
fun TextView.setExerciseText(item: Exercise?){
    item?.let{
        text = item.ex
    }
}






