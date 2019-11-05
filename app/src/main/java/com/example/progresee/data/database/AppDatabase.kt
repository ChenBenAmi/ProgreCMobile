package com.example.progresee.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.progresee.beans.*
import com.example.progresee.data.database.dao.*
import com.example.progresee.utils.ConverterUtils

@TypeConverters(ConverterUtils::class)
@Database(
    entities = [Classroom::class, User::class, Task::class, Exercise::class, FinishedUser::class],
    version = 46,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun classroomDao(): ClassroomDao

    abstract fun exerciseDao(): ExerciseDao

    abstract fun taskDao(): TaskDao

    abstract fun userDao(): UserDao

    abstract fun userFinishedDao(): UserFinishedDao


}
