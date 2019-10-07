package com.example.progresee.data.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Task

@Dao
interface TaskDao {


    @Query("select * from task")
    fun getTasks():LiveData<List<Task>>

    @Insert
    fun insert(task: Task)

}