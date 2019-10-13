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
    val title: String,
    val description: String,
    val imageURL: String,
    val startDate: Date,
    val endDate: Date
//    val exercises: Map<Long,Exercise>
):Serializable