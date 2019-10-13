package com.example.progresee.beans

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Exercise (

    @PrimaryKey
    val id : Long,
    var ex : String,
    var taskId : Long
//    val finishedUsers : Map<Long,FinishedUsers>

):Serializable