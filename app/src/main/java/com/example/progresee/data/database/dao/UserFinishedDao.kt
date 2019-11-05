package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.UserFinished

@Dao
interface UserFinishedDao {


    @Query("SELECT * FROM userFinished where exerciseUid=:exerciseUid")
    fun getFinishedUser(exerciseUid: String): LiveData<List<UserFinished>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFinishedUser(userFinished: UserFinished)



}
