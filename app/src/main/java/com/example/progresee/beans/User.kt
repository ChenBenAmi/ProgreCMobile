package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class User(
    @PrimaryKey
    val uid: String,
    val profilePictureUrl: String,
    val dateCreated:  String,
    val signedIn:  String,
    val fullName: String,
    val email: String
)

data class UserFirestore(
    val uid: String = "",
    val profilePictureUrl: String = "",
    val dateCreated: Date = Date(),
    val signedIn: Date = Date(),
    val fullName: String = "",
    val email: String = ""
)

