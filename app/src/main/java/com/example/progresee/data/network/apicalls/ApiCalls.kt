package com.example.progresee.data.network.apicalls

import com.example.progresee.beans.Classroom
import com.example.progresee.beans.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


interface ApiCalls {

    //User Controller

    @GET("/user/getCurrentUser")
    fun getCurrentUserAsync(@Header("Authorization") token: String?): Deferred<Response<User>>

    @PUT("user/updateUser")
    fun updateUser(@Header("Authorization") token: String?, @Body user: User): Deferred<Response<User>>

    @POST("/user/createClassroom/{name}")
    fun createClassroom(@Header("Authorization") token: String?, @Path("name") name: String): Deferred<Response<Classroom>>

    @PUT("user/addToClassroom")
    fun addToClassroom(
        @Header("Authorization") token: String?, @Query("userId") userId: Long,
        @Query("classroomId") classroomId: Long
    ): Deferred<Response<User>>

    @GET("/user/getUsersInClassroom")
    fun getUsersInClassroom(@Header("Authorization") token: String?, classroomId: Long): Deferred<Response<List<User>>>

    @PUT("/user/updateClassroom")
    fun updateClassroom(@Header("Authorization") token: String?, @Body classroom: Classroom): Deferred<Response<Classroom>>

    @PUT("user/leaveClassroom")
    fun leaveClassRoom(
        @Header("Authorization") token: String?, @Query("classroomId") classroomId: Long
    ): Deferred<Response<User>>

    @PUT("user/removeUser")
    fun removeUser(
        @Header("Authorization") token: String?, @Query("userId") userId: Long,
        @Query("classroomId") classroomId: Long
    ): Deferred<Response<String>>

    @PUT("/user/transferClassroom")
    fun transferClassroom(
        @Header("Authorization") token: String?, @Query("classroomId") classroomId: Long,
        @Query("email") email: String
    ): Deferred<Response<Classroom>>


}
