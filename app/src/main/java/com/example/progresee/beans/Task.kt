package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Task(

    @PrimaryKey
    val id: Long,
    val title: String,
    val description: String,
    val imageURL: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
//    val exercises: Map<Long,Exercise>
)