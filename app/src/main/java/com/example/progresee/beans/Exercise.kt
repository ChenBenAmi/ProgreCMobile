package com.example.progresee.beans


data class Exercise(

    val uid: String = "",
    var exerciseTitle: String = "",
    val dateCreated: String = "",
    var finishedUsersList: Map<String, String> = emptyMap(),
    val taskUid: String = "",
    val archived : Boolean = false

)

