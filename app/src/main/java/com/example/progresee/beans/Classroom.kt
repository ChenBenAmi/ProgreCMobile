package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable
import java.util.*

@Entity
data class Classroom(

    @PrimaryKey
    val id: Long,
    @Json(name="name")
    val name: String,
    val owner: String,
    val dateCreated: String,
    val openTasks:Int): Serializable




