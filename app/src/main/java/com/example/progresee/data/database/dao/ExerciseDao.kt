package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.progresee.beans.Exercise

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise where taskUid=:taskId")
    fun getExercises(taskId:String): LiveData<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercise(exercise: Exercise)

    @Update
    fun updateExercise(exercise:Exercise)

    @Query("delete from exercise where uid=:exerciseId")
    fun deleteExercise(exerciseId:String)



}