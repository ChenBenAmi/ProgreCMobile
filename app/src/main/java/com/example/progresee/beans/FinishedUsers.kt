package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity
data class FinishedUsers(
    @PrimaryKey
    val id: Long,
    val fullName: String,
    val dateCompleted: Date

)