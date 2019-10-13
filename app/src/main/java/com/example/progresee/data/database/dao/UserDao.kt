package com.example.progresee.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progresee.beans.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(data: User?)

    @Query("select * from User where id = :userId")
    fun getUser(userId: Long?): LiveData<User?>

    @Query("select * from User where id = :userId")
    fun isUserExist(userId: Long?): Boolean

    //TODO: maybe OneToMany relationship for classroom lists of users
    @Query("select * from User")
    fun getAllUsers(): LiveData<User?>


}