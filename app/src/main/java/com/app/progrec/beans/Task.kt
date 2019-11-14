package com.app.progrec.beans


data class Task(

    val uid: String = "",
    var title: String = "",
    var description: String = "",
    var referenceLink: String? = "",
    val startDate: String="",
    var endDate: String="",
    val classroomUid: String = "",
    var completed: Boolean = false,
    val archived : Boolean = false


)