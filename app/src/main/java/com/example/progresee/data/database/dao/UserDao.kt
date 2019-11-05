package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.progresee.beans.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(data: User?)

    @Query("select * from User where uid = :userId")
    fun getUser(userId: String?): LiveData<User?>

    @Query("select * from User where uid = :userId")
    fun isUserExist(userId: String?): Boolean

    @Query("select * from User")
    fun getAllUsers(): LiveData<List<User?>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(data: List<User>?)

    @Query("delete from User")
    fun clearUsers()
    @Query("delete from user where uid=:userId")
    fun removeUser(userId: String)


}