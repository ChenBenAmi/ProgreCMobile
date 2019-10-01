package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
@Entity
data class FinishedUsers(
    @PrimaryKey
    val id: Long,
    val fullName: String,
    val dateCompleted: LocalDateTime

)