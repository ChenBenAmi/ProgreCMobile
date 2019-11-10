package com.example.progresee.beans

import java.util.*

data class Classroom(
    val uid: String = "",
    var name: String = "",
    var owner: String ="",
    var ownerUid: String="",
    var userList: Map<String, String> = emptyMap(),
    val dateCreated: String = "",
    var description: String = "",
    var numberOfTasks: Int = 0,
    val archived : Boolean = false
)







