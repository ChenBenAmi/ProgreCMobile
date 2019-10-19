package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.progresee.beans.Classroom
import com.example.progresee.utils.ConverterUtils
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

@Entity
data class User(
    @PrimaryKey
    val uid: String,
    var email: String,
    var fullName: String,
    var profilePictureUrl: String,
    var dateCreated:Date,
    var signedIn:Date


)