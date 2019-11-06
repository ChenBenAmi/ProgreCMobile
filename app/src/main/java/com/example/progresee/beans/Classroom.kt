package com.example.progresee.beans

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.progresee.adapters.UserClickListener
import com.google.gson.annotations.SerializedName
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
    var userList: Map<String,String>,
    val dateCreated: String,
    var description : String,
    var numberOfTasks: Int
)


data class ClassroomFirestore(
    val uid: String = "",
    var name: String = "",
    var owner: String ="",
    var ownerUid: String="",
    var userList: Map<String, String> = emptyMap(),
    val dateCreated: Date = Date(),
    var description: String = "",
    var numberOfTasks: Int = 0
)







