package com.example.progresee.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.*
import com.example.progresee.data.network.ApiService
import com.example.progresee.data.network.apicalls.ApiCalls
import com.firebase.ui.firestore.paging.FirestoreDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber

class AppRepository constructor(

    private val network: ApiService
) {

    private val firestoreDB = FirebaseFirestore.getInstance()
    fun getFirestoreDB() = firestoreDB
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var currentUserEmail = firebaseAuth.currentUser?.email
    fun getCurrentUserEmail() = currentUserEmail

    fun setUserEmail() {
        currentUserEmail=firebaseAuth.currentUser?.email
    }


    private var _isAdmin = MutableLiveData<Boolean?>()
    val isAdmin
        get() = _isAdmin

    private var _currentToken = MutableLiveData<String?>()
    val currentToken: LiveData<String?>
        get() = _currentToken

    fun removeToken() {
        _currentToken.value = null
    }

    fun setToken(token: String) {
        _currentToken.value = token
    }

    private val currentUser = MutableLiveData<User>()
    fun getUser() = currentUser

    fun setCurrentUser(user: User) {
        currentUser.value = user
    }

    private val apiCalls: ApiCalls = network.retrofit()


    fun isAdmin(): Boolean {
        _isAdmin.value = true
        return true
    }

    fun notAdmin(): Boolean {
        _isAdmin.value = false
        return false
    }


    fun getCurrentUserAsync(token: String): Deferred<Response<User>> {
        return apiCalls.getCurrentUserAsync(token)

    }

    fun updateUser(token: String, user: User): Deferred<Response<User>> {
        return apiCalls.updateUserAsync(token, user)
    }


    fun createClassroomAsync(
        token: String,
        name: String,
        description: String
    ): Deferred<Response<Map<String, Classroom>>> {
        return apiCalls.createClassroomAsync(token, name, description)
    }

    fun deleteClassroomAsync(
        token: String,
        classroomId: String
    ): Deferred<Response<Map<String, String>>> {
        return apiCalls.deleteClassroomAsync(token, classroomId)
    }

    fun addToClassroomAsync(
        token: String,
        classroomId: String,
        email: String
    ): Deferred<Response<Map<String, Classroom>>> {
        return apiCalls.addToClassroomAsync(token, classroomId, email)
    }

    fun getUsersInClassroomAsync(
        token: String,
        classroomId: String
    ): Deferred<Response<Map<String, User>>> {
        return apiCalls.getUsersInClassroomAsync(token, classroomId)
    }

    fun updateClassroomAsync(
        token: String,
        classroomId: String,
        name: String, description: String
    ): Deferred<Response<Map<String, Classroom>>> {
        return apiCalls.updateClassroomAsync(token, classroomId, name, description)
    }

    fun leaveClassroom(token: String, classroomId: String): Deferred<Response<User>> {
        return apiCalls.leaveClassRoomAsync(token, classroomId)
    }

    fun removeUserAsync(
        token: String,
        classroomId: String,
        userId: String
    ): Deferred<Response<Map<String, String>>> {
        return apiCalls.removeUserAsync(token, classroomId, userId)
    }

    fun transferClassroomAsync(
        token: String,
        classroomId: String,
        newOwnerId: String
    ): Deferred<Response<Map<String, Classroom>>> {
        return apiCalls.transferClassroomAsync(token, classroomId, newOwnerId)
    }

    fun getClassroom(token: String, classroomId: String): Deferred<Response<Classroom>> {
        return apiCalls.getClassroomAsync(token, classroomId)
    }

    fun getClassroomsAsync(token: String): Deferred<Response<Map<String, Classroom>>> {
        return apiCalls.getClassroomsAsync(token)
    }


    fun getAllTasksAsync(
        token: String,
        classroomId: String
    ): Deferred<Response<Map<String, Task>>> {
        return apiCalls.getAllTasksAsync(token, classroomId)
    }

    fun getTaskAsync(
        token: String, classroomId: String,
        taskId: String
    ): Deferred<Response<Map<String, Task>>> {
        return apiCalls.getTaskAsync(token, classroomId, taskId)
    }

    fun createTaskAsync(
        token: String,
        classroomId: String,
        title: String,
        description: String,
        link: String,
        date: String
    ): Deferred<Response<Map<String, Task>>> {
        return apiCalls.createTaskAsync(token, classroomId, title, description, link, date)
    }

    fun deleteTaskAsync(
        token: String, classroomId: String,
        taskId: String
    ): Deferred<Response<Map<String, String>>> {
        return apiCalls.deleteTaskAsync(token, classroomId, taskId)
    }

    fun updateTaskAsync(
        token: String,
        classroomId: String,
        task: Task
    ): Deferred<Response<Map<String, Task>>> {
        return apiCalls.updateTaskAsync(token, classroomId, task)
    }

    fun getAllExercisesAsync(
        token: String, classroomId: String,
        taskId: String
    ): Deferred<Response<Map<String, Exercise>>> {
        return apiCalls.getAllExercisesAsync(token, classroomId, taskId)
    }

    fun getExerciseAsync(
        token: String, classroomId: String, taskId: String, exerciseId: String
    ): Deferred<Response<Map<String, Exercise>>> {
        return apiCalls.getExerciseAsync(token, classroomId, taskId, exerciseId)
    }

    fun getFinishedUsersAsync(
        token: String, classroomId: String, exerciseId: String
    ): Deferred<Response<Map<String, String>>> {
        return apiCalls.getFinishedUsersAsync(token, classroomId, exerciseId)
    }

    fun createExerciseAsync(
        token: String, classroomId: String,
        taskId: String, description: String
    ): Deferred<Response<Map<String, Exercise>>> {
        return apiCalls.createExerciseAsync(token, classroomId, taskId, description)
    }

    fun deleteExerciseAsync(
        token: String, classroomId: String, taskId: String, exerciseId: String
    ): Deferred<Response<Map<String, String>>> {
        return apiCalls.deleteExerciseAsync(token, classroomId, taskId, exerciseId)
    }

    fun updateExerciseAsync(
        token: String, classroomId: String, taskId: String, exercise: Exercise
    ): Deferred<Response<Map<String, Exercise>>> {
        return apiCalls.updateExerciseAsync(token, classroomId, taskId, exercise)
    }

    fun updateStatusAsync(
        token: String, classroomId: String, taskId: String, exerciseId: String
    ): Deferred<Response<Map<String, Exercise>>> {
        return apiCalls.updateStatusAsync(token, classroomId, taskId, exerciseId)
    }



}