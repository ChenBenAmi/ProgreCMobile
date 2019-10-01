package com.example.progresee.beans

import java.time.LocalDateTime

data class Classroom (
    val id : Long,
    val name : String,
    val owner : String,
    val dateCreated : LocalDateTime,
    val tasks : Map<Long,Task>

)