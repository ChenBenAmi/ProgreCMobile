package com.example.progresee.data

import androidx.lifecycle.LiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.data.database.AppDatabase

class AppRepository constructor(private val dataBase: AppDatabase) {

val classrooms:LiveData<List<Classroom>> = dataBase.classroomDao().getClassrooms()

    fun insertClassroom(classroom: Classroom) {
        dataBase.classroomDao().insert(classroom)
    }

}