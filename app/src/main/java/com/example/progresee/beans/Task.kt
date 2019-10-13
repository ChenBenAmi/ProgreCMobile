package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
data class Task(

    @PrimaryKey
    val id: Long,
    var title: String,
    var description: String,
    var imageURL: String,
    val startDate: Date,
    var endDate: Date
//    val exercises: Map<Long,Exercise>
):Serializable