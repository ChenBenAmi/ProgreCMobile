package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.progresee.beans.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(data: User?)

    @Query("select * from User where uid = :userId")
    fun getUser(userId: String?): LiveData<User?>

    @Query("select * from User where uid = :userId")
    fun isUserExist(userId: String?): Boolean

    //TODO: maybe OneToMany relationship for classroom lists of users
    @Query("select * from User")
    fun getAllUsers(): LiveData<List<User?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(data: List<User>?)


}