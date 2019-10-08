package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progresee.beans.Exercise

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise")
    fun getExercises(): LiveData<List<Exercise>>

    @Insert
    fun insertExercise(exercise: Exercise)
}