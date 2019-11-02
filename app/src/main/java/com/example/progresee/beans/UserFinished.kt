package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserFinished(

    @PrimaryKey
    var uid: String,
    var hasFinished: Boolean,
    var timestamp: String,
    var exerciseUid: String,
    var email: String


)