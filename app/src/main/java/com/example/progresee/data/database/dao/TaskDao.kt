package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progresee.beans.Task

@Dao
interface TaskDao {


    @Query("select * from task")
    fun getTasks():LiveData<List<Task>>

    @Query("select * from task where Id = :taskId")
    fun getTask(taskId: Long?): LiveData<Task>

    @Insert
    fun insert(task: Task)

}