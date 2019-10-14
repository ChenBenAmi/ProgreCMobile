package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.progresee.beans.Classroom

@Dao
interface ClassroomDao {

    @Query("select * from classroom")
    fun getClassrooms(): LiveData<List<Classroom?>>

    @Insert
    fun insert(classroom: Classroom?)

    @Query("select * from classroom where Id = :classroomId")
    fun getClassroom(classroomId: Long?): LiveData<Classroom?>

    @Update
    fun updateClassroom(classroom: Classroom?)

    @Delete
    fun deleteClassroom(classroom: Classroom?)
    @Query("delete from classroom where Id= :classroomId ")
    fun deleteClassroomById(classroomId: Long?)
}
