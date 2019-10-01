package com.example.progresee.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.progresee.beans.*
import com.example.progresee.data.database.Dao.*
import com.example.progresee.utils.ConverterUtils

@TypeConverters(ConverterUtils::class)
@Database(entities = [Classroom::class,User::class,FinishedUsers::class,Task::class,Exercise::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val classroomDao: ClassroomDao

    abstract  val exerciseDao:ExerciseDao

    abstract val taskDao:TaskDao

    abstract val userDao:UserDao

    abstract  val finishedUsersDao:FinishedUsersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "ProgreSeeDB")
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
            }
            return instance
        }
    }

}
