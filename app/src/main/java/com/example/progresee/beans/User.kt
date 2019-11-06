package com.example.progresee.beans

import java.util.*

data class User(
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
    val dateCreated: String = "",
    val signedIn: String = "",
    val fullName: String = "",
    val email: String = ""
)

