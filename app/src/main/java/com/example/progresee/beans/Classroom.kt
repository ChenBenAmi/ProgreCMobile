package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Classroom(

    //TODO remove auto generate when you get network calls working
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val owner: String,
    val dateCreated: Date,
    val openTasks:Int)




