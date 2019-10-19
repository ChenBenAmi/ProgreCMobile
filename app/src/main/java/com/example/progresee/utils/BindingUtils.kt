package com.example.progresee.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.progresee.R
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.beans.User


@BindingAdapter("setFullName")
fun TextView.setFullName(user: User?) {
    user?.let {
        text = user.fullName
    }
}

@BindingAdapter("setEmail")
fun TextView.setEmail(user: User?) {
    user?.let {
        text = user.email
    }
}

@BindingAdapter("setLastLoggedIn")
fun TextView.setLastLoggedIn(user: User?) {
    user?.let {
        text = user.signedIn.toString()
    }
}

@BindingAdapter("setProfilePic")
fun ImageView.setProfilePic(user: User?) {
    user?.let {
        Glide.with(context)
            .load(user.profilePictureUrl)
            .into(this)
    }
}


@BindingAdapter("classroomOwner")
fun TextView.setClassroomOwner(item: Classroom?) {
    item?.let {
        text = item.owner
    }
}

//TODO find a way to get the number of open tasks
//@BindingAdapter("numberOfOpenTasks")
//fun TextView.setNumberOfOpenTasks(item: Classroom?) {
//    item?.let {
//        text = context.getString(R.string.number_of_tasks, item.openTasks)
//    }
//}


@BindingAdapter("classroomName")
fun TextView.setClassroomName(item: Classroom?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("taskTitle")
fun TextView.setTaskTitle(item: Task?) {
    item?.let {
        text = item.title
    }
}

@BindingAdapter("taskDueDate")
fun TextView.setTaskDueDate(item: Task?) {
    item?.let {
        text = context.getString(R.string.due_by, item.endDate.toString())
    }
}

@BindingAdapter("taskDescription")
fun TextView.setTaskDescription(item: Task?) {
    item?.let {
        text = item.description
    }
}

@BindingAdapter("taskImage")
fun ImageView.setTaskImage(item: Task?) {
    item?.let {
        Glide.with(context)
            .load(item.imageUrls[0])
            .into(this)
    }
}

@BindingAdapter("exerciseText")
fun TextView.setExerciseText(item: Exercise?) {
    item?.let {
        text = item.exerciseTitle
    }
}

@BindingAdapter("classroomId")
fun TextView.setClassroomId(item: Classroom?) {
    item?.let {
        text = item.uid.toString()
    }
}







