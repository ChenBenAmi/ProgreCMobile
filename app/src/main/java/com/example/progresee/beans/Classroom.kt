package com.example.progresee.beans

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime

@Entity
data class Classroom(

    @PrimaryKey
    val id: Long,
    val name: String,
    val owner: String,
    val dateCreated: LocalDateTime
//    val tasks: List<Task>



)

//class Converter {
//    @TypeConverter
//    fun fromTaksJson(stat: List<Task>): String {
//        return Gson().toJson(stat)
//    }
//
//    @TypeConverter
//    fun toTaskList(jsonTasks: String): List<Task> {
//        val notesType = object : TypeToken<List<Task>>() {}.type
//        return Gson().fromJson<List<Task>>(jsonTasks, notesType)
//    }
//
//}




