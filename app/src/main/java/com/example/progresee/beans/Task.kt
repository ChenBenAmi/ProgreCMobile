package com.example.progresee.beans

import java.time.LocalDateTime

data class Task(

    val id: Long,
    val title: String,
    val description: String,
    val imageURL: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val exercises: Map<Long,Exercise>
)