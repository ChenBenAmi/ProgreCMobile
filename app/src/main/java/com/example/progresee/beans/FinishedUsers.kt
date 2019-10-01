package com.example.progresee.beans

import java.time.LocalDateTime

data class FinishedUsers(

    val id: Long,
    val fullName: String,
    val dateCompleted: LocalDateTime

)