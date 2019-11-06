package com.example.progresee.beans

import java.util.*

data class Task(

    val uid: String,
    var title: String,
    var description: String,
    var referenceLink: String?,
    val startDate: String,
    var endDate: String,
    val classroomUid: String,
    var status: Boolean

)

data class TaskFirestore(

    val uid: String = "",
    var title: String = "",
    var description: String = "",
    var referenceLink: String? = "",
    val startDate: String="",
    var endDate: String="",
    val classroomUid: String = "",
    var status: Boolean = false

)