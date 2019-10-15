package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.progresee.beans.Classroom
import com.example.progresee.utils.ConverterUtils
import java.io.Serializable
import java.time.LocalDateTime

@Entity
data class User(
    @PrimaryKey
    val id: Long,
    val uid:String,
    var email: String,
    var fullName: String,
    var pictureURL: String,
    var role:String,
    var dateCreated:String,
    var lastLoggedIn:String,
    var  classrooms:Map<Long,Classroom>

):Serializable