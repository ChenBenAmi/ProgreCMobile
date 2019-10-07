package com.example.progresee.beans

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.util.*

@Entity
data class Classroom(

    @PrimaryKey
    val id: Long,
    val name: String,
    val owner: String,
    val dateCreated: Date,
    val openTasks:Int)




