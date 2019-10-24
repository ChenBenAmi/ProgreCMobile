package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise (

    @PrimaryKey
    val uid : String,
    var exerciseTitle : String,
    val dateCreated:DateCreated,
    var finishedUsersList:List<String>,
    val taskUid: String

)