package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Exercise(

    @PrimaryKey
    val uid: String,
    var exerciseTitle: String,
    val dateCreated: String,
    var finishedUsersList: Map<String, String>,
    val taskUid: String

)

data class ExerciseFirestore(

    val uid: String = "",
    var exerciseTitle: String = "",
    val dateCreated: Date = Date(),
    var finishedUsersList: Map<String, String> = emptyMap(),
    val taskUid: String = ""

)

