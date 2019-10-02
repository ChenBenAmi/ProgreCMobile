package com.example.progresee.beans

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime

@Entity
data class Classroom(

    @PrimaryKey
    val id: Long,
    val name: String,
    val owner: String,
    val dateCreated: LocalDateTime,
    val openTasks:Int)




