package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.progresee.adapters.UserClickListener
import com.squareup.moshi.Json
import java.io.Serializable
import java.util.*

@Entity
data class Classroom(

    @PrimaryKey
    val uid: String,
    var name: String,
    var owner: String,
    var ownerUid: String,
    var userList: List<String>,
    val dateCreated: Date
)




