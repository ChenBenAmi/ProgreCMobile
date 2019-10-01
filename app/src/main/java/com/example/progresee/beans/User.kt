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
    val facebookId: String,
    val googleId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val pictureURL: String,
//    val classRooms: Map<Long, Classroom>,
    val dateCreated: LocalDateTime,
    val lastLoggedIn: LocalDateTime
)