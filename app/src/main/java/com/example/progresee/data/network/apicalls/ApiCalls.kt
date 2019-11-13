package com.example.progresee.data.network.apicalls

import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.beans.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


interface ApiCalls {

    //User Controller

    @GET("/user/getCurrentUser")
    fun getCurrentUserAsync(@Header("Authorization") token: String): Deferred<Response<User>>



    @GET("user/getClassrooms")
    fun getClassroomsAsync(
        @Header("Authorization") token: String
    ): Deferred<Response<Map<String, Classroom>>>

    @POST("/user/createClassroom")
    fun createClassroomAsync(
        @Header("Authorization") token: String, @Query("name") name: String,
        @Query("description") description: String

    ): Deferred<Response<Classroom>>

    @PUT("/user/updateClassroom")
    fun updateClassroomAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "name"
        ) name: String, @Query("description") description: String
    ): Deferred<Response<Classroom>>

    @DELETE("/user/deleteClassroom")
    fun deleteClassroomAsync(@Header("Authorization") token: String?, @Query("classroomId") classroomId: String):
            Deferred<Response<String>>

    @GET("/user/getUsersInClassroom")
    fun getUsersInClassroomAsync(@Header("Authorization") token: String?, @Query("classroomId") classroomId: String):
            Deferred<Response<Map<String, User>>>

    @PUT("/user/transferClassroom")
    fun transferClassroomAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String,
        @Query("newOwnerId") newOwnerId: String
    ): Deferred<Response<Classroom>>

    @PUT("user/addToClassroom")
    fun addToClassroomAsync(
        @Header("Authorization") token: String?, @Query("classroomId") classroomId: String,
        @Query("email") email: String
    ): Deferred<Response<Classroom>>

    @PUT("user/leaveClassroom")
    fun leaveClassRoomAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String
    ): Deferred<Response<String>>

    @PUT("user/removeUser")
    fun removeUserAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query("userId") userId: String
    ): Deferred<Response<String>>








    //Task Controller
    @GET("task/getAllTasks")
    fun getAllTasksAsync(@Header("Authorization") token: String, @Query("classroomId") classroomId: String): Deferred<Response<Map<String, Task>>>


    @POST("task/createTask")
    fun createTaskAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "title"
        ) title: String, @Query("description") description: String, @Query("link") link: String, @Query(
            "date"
        ) date: String
    ): Deferred<Response<Task>>

    @DELETE("task/deleteTask")
    fun deleteTaskAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String
    ): Deferred<Response<String>>


    @PUT("task/updateTask")
    fun updateTaskAsync(@Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Body task: Task):
            Deferred<Response<Task>>








    //Exercise Controller
    @GET("exercise/getAllExercises")
    fun getAllExercisesAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String
    ): Deferred<Response<Map<String, Exercise>>>


    @POST("exercise/createExercise")
    fun createExerciseAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Query("description") description: String
    ): Deferred<Response<Exercise>>

    @DELETE("exercise/deleteExercise")
    fun deleteExerciseAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Query("exerciseId") exerciseId: String
    ): Deferred<Response<String>>

    @PUT("exercise/updateExercise")
    fun updateExerciseAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Body exercise: Exercise
    ): Deferred<Response<Exercise>>

    @PUT("exercise/updateStatus")
    fun updateStatusAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query(
            "taskId"
        ) taskId: String, @Query("exerciseId") exerciseId: String
    ): Deferred<Response<Exercise>>

    @GET("exercise/getFinishedUsers")
    fun getFinishedUsersAsync(
        @Header("Authorization") token: String, @Query("classroomId") classroomId: String, @Query("exerciseId") exerciseId: String
    ): Deferred<Response<Map<String, String>>>


}
