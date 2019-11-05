package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FinishedUser(

    @PrimaryKey
    var email: String,
    var timestamp: String,
    var exerciseId: String


)