package com.example.progresee.beans

data class Exercise (

    val ex : String,
    val taskId : Long,
    val finishedUsers : Map<Long,FinishedUsers>,
    val id : Int
)