package com.example.progresee.data.network.apicalls

import com.example.progresee.beans.Classroom
import com.example.progresee.beans.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


interface ApiCalls {

    //User Controller

    @GET("/user/getCurrentUser")
    fun getCurrentUserAsync(@Header("Authorization") token: String): Deferred<Response<User>>

    @PUT("user/updateUser")
    fun updateUserAsync(@Header("Authorization") token: String, @Body user: User): Deferred<Response<User>>

    @GET("user/getClassroom")
    fun getClassroomAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String
    ): Deferred<Response<Classroom>>

    @GET("user/getClassrooms")
    fun getClassroomsAsync(
        @Header("Authorization") token: String
    ): Deferred<Response<Map<String, Classroom>>>

    @POST("/user/createClassroom")
    fun createClassroomAsync(@Header("Authorization") token: String, @Query("name") name: String): Deferred<Response<Map<String, Classroom>>>

    @PUT("/user/updateClassroom")
    fun updateClassroomAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "name"
        ) name: String
    ): Deferred<Response<Map<String, Classroom>>>

    @DELETE("/user/deleteClassroom")
    fun deleteClassroomAsync(@Header("Authorization") token: String?, @Query("classroomId") classroomId: String): Deferred<Response<Map<String, String>>>

    @GET("/user/getUsersInClassroom")
    fun getUsersInClassroomAsync(@Header("Authorization") token: String?, @Query("classroomId") classroomId: String): Deferred<Response<Map<String, User>>>

    @PUT("/user/transferClassroom")
    fun transferClassroomAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String,
        @Query("newOwnerId") newOwnerId: String
    ): Deferred<Response<Map<String, Classroom>>>

    @PUT("user/addToClassroom")
    fun addToClassroomAsync(
        @Header("Authorization") token: String?, @Query("classroomId") classroomId: String,
        @Query("email") email: String
    ): Deferred<Response<Map<String, Classroom>>>


    @PUT("user/leaveClassroom")
    fun leaveClassRoomAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String
    ): Deferred<Response<User>>

    @PUT("user/removeUser")
    fun removeUserAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query("userId") userId: String
    ): Deferred<Response<Map<String,Classroom>>>


}
