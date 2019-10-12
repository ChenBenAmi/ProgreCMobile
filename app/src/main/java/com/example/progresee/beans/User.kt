package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.progresee.beans.Classroom
import com.example.progresee.utils.ConverterUtils
import java.time.LocalDateTime

@Entity
data class User(
    @PrimaryKey
    val id: Long,
    val uid:String,
    val email: String,
    val fullName: String,
    val pictureURL: String,
    val role:String,
    val dateCreated:String,
    val lastLoggedIn:String

)