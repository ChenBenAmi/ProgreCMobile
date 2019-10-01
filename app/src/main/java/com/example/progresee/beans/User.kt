package com.example.progresee.beans

import com.example.progresee.beans.Classroom
import java.time.LocalDateTime

data class User(

    val id: Long,
    val facebookId: String,
    val googleId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val pictureURL: String,
    val role: Role,
    val classRooms: Map<Long,Classroom>,
    val loginType: List<LoginType>,
    val dateCreated: LocalDateTime,
    val lastLoggedIn: LocalDateTime


)