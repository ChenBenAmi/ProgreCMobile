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
    var name: String,
    var owner: String,
    var dateCreated: String,
    var openTasks:Int): Serializable




