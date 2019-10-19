package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Exercise (

    @PrimaryKey
    val uid : String,
    var exerciseTitle : String,
    val dateCreated:Date,
    var finishedUsersList:List<String>

)