package com.example.progresee.beans

import java.util.*

data class Exercise(

    val uid: String,
    var exerciseTitle: String,
    val dateCreated: String,
    var finishedUsersList: Map<String, String>,
    val taskUid: String

)

data class ExerciseFirestore(

    val uid: String = "",
    var exerciseTitle: String = "",
    val dateCreated: String = "",
    var finishedUsersList: Map<String, String> = emptyMap(),
    val taskUid: String = ""

)

