package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.progresee.beans.FinishedUser

@Dao
interface UserFinishedDao {


    @Query("SELECT * FROM finisheduser where exerciseId=:exerciseUid")
    fun getFinishedUser(exerciseUid: String): LiveData<List<FinishedUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFinishedUser(userFinished: FinishedUser)


}
