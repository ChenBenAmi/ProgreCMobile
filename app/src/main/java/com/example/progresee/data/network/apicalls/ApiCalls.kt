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
    fun updateUserAsync(@Header("Authorization") token: String?, @Body user: User): Deferred<Response<User>>

    @POST("/user/createClassroom")
    fun createClassroomAsync(@Header("Authorization") token: String?, @Query("name") name: String): Deferred<Response<Classroom>>

    @PUT("user/addToClassroom")
    fun addToClassroomAsync(
        @Header("Authorization") token: String?, @Query("userId") userId: Long,
        @Query("classroomId") classroomId: Long
    ): Deferred<Response<User>>

    @GET("/user/getUsersInClassroom")
    fun getUsersInClassroomAsync(@Header("Authorization") token: String?, classroomId: Long): Deferred<Response<List<User>>>

    @PUT("/user/updateClassroom")
    fun updateClassroomAsync(@Header("Authorization") token: String?, @Body classroom: Classroom): Deferred<Response<Classroom>>

    @PUT("user/leaveClassroom")
    fun leaveClassRoomAsync(
        @Header("Authorization") token: String?, @Query("classroomId") classroomId: Long
    ): Deferred<Response<User>>

    @PUT("user/removeUser")
    fun removeUserAsync(
        @Header("Authorization") token: String?, @Query("userId") userId: Long,
        @Query("classroomId") classroomId: Long
    ): Deferred<Response<String>>

    @PUT("/user/transferClassroom")
    fun transferClassroomAsync(
        @Header("Authorization") token: String?, @Query("classroomId") classroomId: Long,
        @Query("email") email: String
    ): Deferred<Response<Classroom>>


}
