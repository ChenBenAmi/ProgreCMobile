package com.example.progresee.data.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progresee.beans.Classroom

@Dao
interface ClassroomDao {

    @Query("select * from classroom")
    fun getClassrooms(): LiveData<List<Classroom>>

    @Insert
    fun insert(classroom: Classroom)

}
