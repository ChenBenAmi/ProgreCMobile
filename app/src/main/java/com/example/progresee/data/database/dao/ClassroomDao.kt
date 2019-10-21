package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.progresee.beans.Classroom

@Dao
interface ClassroomDao {

    @Query("select * from classroom")
    fun getClassrooms(): LiveData<List<Classroom>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(classroom: Classroom?)

    @Query("select * from classroom where uid = :classroomId")
    fun getClassroom(classroomId: String): LiveData<Classroom?>

    @Update
    fun updateClassroom(classroom: Classroom?)

    @Delete
    fun deleteClassroom(classroom: Classroom?)

    @Query("delete from classroom where uid= :classroomId ")
    fun deleteClassroomById(classroomId: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<Classroom>)
}
