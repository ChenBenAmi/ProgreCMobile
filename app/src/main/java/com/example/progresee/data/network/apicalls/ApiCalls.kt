package com.example.progresee.data.network.apicalls

import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
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
    fun createClassroomAsync(
        @Header("Authorization") token: String, @Query("name") name: String,
        @Query("description") description: String

    ): Deferred<Response<Map<String, Classroom>>>

    @PUT("/user/updateClassroom")
    fun updateClassroomAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "name"
        ) name: String, @Query("description") description: String
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
    ): Deferred<Response<Map<String, Classroom>>>

    //Task Controller
    @GET("task/getAllTasks")
    fun getAllTasksAsync(@Header("Authorization") token: String, @Query("classroomId") classroomId: String): Deferred<Response<Map<String, Task>>>

    @GET("task/getTask")
    fun getTaskAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String
    ): Deferred<Response<Map<String, Task>>>

    @POST("task/createTask")
    fun createTaskAsync(@Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Body task: Task): Deferred<Response<Map<String, Task>>>

    @DELETE("task/deleteTask")
    fun deleteTaskAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String
    ): Deferred<Response<Map<String, String>>>


    @PUT("task/updateTask")
    fun updateTaskAsync(@Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Body task: Task): Deferred<Response<Map<String, Task>>>


    //Exercise Controller

    @GET("exercise/getAllExercises")
    fun getAllExercisesAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String
    ): Deferred<Response<Map<String, Exercise>>>

    @GET("exercise/getExercise")
    fun getExerciseAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Query("exerciseId") exerciseId: String
    ): Deferred<Response<Map<String, Exercise>>>

    @GET("exercise/getFinishedUsers")
    //TODO change return type
    fun getFinishedUsersAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Query("exerciseId") exerciseId: String
    ): Deferred<Response<Map<String, Exercise>>>

    @POST("exercise/createExercise")
    fun createExerciseAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Body exercise: Exercise
    ): Deferred<Response<Map<String, Exercise>>>

    @DELETE("exercise/deleteExercise")
    fun deleteExerciseAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Query("exerciseId") exerciseId: String
    ): Deferred<Response<Map<String, String>>>

    @PUT("exercise/updateExercise")
    fun updateExerciseAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String,@Body exercise: Exercise
    ): Deferred<Response<Map<String, Exercise>>>

    //TODO change return type
    @PUT("exercise/updateStatus")
    fun updateStatusAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Query("exerciseId") exerciseId: String
    ): Deferred<Response<Map<String, String>>>

}
