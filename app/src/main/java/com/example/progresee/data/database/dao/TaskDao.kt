package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Task

@Dao
interface TaskDao {


    @Query("select * from task where classroomUid=:classroomId")
    fun getTasks(classroomId:String):LiveData<List<Task>>

    @Query("select * from task where uid = :taskId")
    fun getTask(taskId: String): LiveData<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Update
    fun updateClassroom(classroom: Classroom?)

    @Query("delete from task where uid=:taskId")
    fun deleteTask(taskId: String)

}