package com.example.progresee.beans

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Exercise (

    @PrimaryKey
    val uid : String,
    var exerciseTitle : String,
    val dateCreated:DateCreated,
    var finishedUsersList:List<String>

)