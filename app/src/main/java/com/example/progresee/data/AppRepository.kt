package com.example.progresee.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.beans.User
import com.example.progresee.data.network.ApiService
import com.example.progresee.data.network.apicalls.ApiCalls
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Deferred
import retrofit2.Response

class AppRepository constructor(
    network: ApiService
) {
    private val apiCalls: ApiCalls = network.retrofit()

    private val firestoreDB = FirebaseFirestore.getInstance()
    fun getFirestoreDB() = firestoreDB
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var currentUserEmail = firebaseAuth.currentUser?.email
    fun getCurrentUserEmail() = currentUserEmail

    fun setUserEmail() {
        currentUserEmail = firebaseAuth.currentUser?.email
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

    fun getClassroomsAsync(token: String): Deferred<Response<Map<String, Classroom>>> {
        return apiCalls.getClassroomsAsync(token)
    }


    fun createClassroomAsync(
        token: String,
        name: String,
        description: String
    ): Deferred<Response<Classroom>> {
        return apiCalls.createClassroomAsync(token, name, description)
    }


    fun updateClassroomAsync(
        token: String,
        classroomId: String,
        name: String, description: String
    ): Deferred<Response<Classroom>> {
        return apiCalls.updateClassroomAsync(token, classroomId, name, description)
    }

    fun deleteClassroomAsync(
        token: String,
        classroomId: String
    ): Deferred<Response<String>> {
        return apiCalls.deleteClassroomAsync(token, classroomId)
    }


    fun getUsersInClassroomAsync(
        token: String,
        classroomId: String
    ): Deferred<Response<Map<String, User>>> {
        return apiCalls.getUsersInClassroomAsync(token, classroomId)
    }

    fun transferClassroomAsync(
        token: String,
        classroomId: String,
        newOwnerId: String
    ): Deferred<Response<Classroom>> {
        return apiCalls.transferClassroomAsync(token, classroomId, newOwnerId)
    }

    fun addToClassroomAsync(
        token: String,
        classroomId: String,
        email: String
    ): Deferred<Response<Classroom>> {
        return apiCalls.addToClassroomAsync(token, classroomId, email)
    }

    fun leaveClassroom(token: String, classroomId: String): Deferred<Response<String>> {
        return apiCalls.leaveClassRoomAsync(token, classroomId)
    }

    fun removeUserAsync(
        token: String,
        classroomId: String,
        userId: String
    ): Deferred<Response<String>> {
        return apiCalls.removeUserAsync(token, classroomId, userId)
    }


    fun getAllTasksAsync(
        token: String,
        classroomId: String
    ): Deferred<Response<Map<String, Task>>> {
        return apiCalls.getAllTasksAsync(token, classroomId)
    }


    fun createTaskAsync(
        token: String,
        classroomId: String,
        title: String,
        description: String,
        link: String,
        date: String
    ): Deferred<Response<Task>> {
        return apiCalls.createTaskAsync(token, classroomId, title, description, link, date)
    }

    fun deleteTaskAsync(
        token: String, classroomId: String,
        taskId: String
    ): Deferred<Response<String>> {
        return apiCalls.deleteTaskAsync(token, classroomId, taskId)
    }

    fun updateTaskAsync(
        token: String,
        classroomId: String,
        task: Task
    ): Deferred<Response<Task>> {
        return apiCalls.updateTaskAsync(token, classroomId, task)
    }


    fun getAllExercisesAsync(
        token: String, classroomId: String,
        taskId: String
    ): Deferred<Response<Map<String, Exercise>>> {
        return apiCalls.getAllExercisesAsync(token, classroomId, taskId)
    }

    fun createExerciseAsync(
        token: String, classroomId: String,
        taskId: String, description: String
    ): Deferred<Response<Exercise>> {
        return apiCalls.createExerciseAsync(token, classroomId, taskId, description)
    }

    fun deleteExerciseAsync(
        token: String, classroomId: String, taskId: String, exerciseId: String
    ): Deferred<Response<String>> {
        return apiCalls.deleteExerciseAsync(token, classroomId, taskId, exerciseId)
    }

    fun updateExerciseAsync(
        token: String, classroomId: String, taskId: String, exercise: Exercise
    ): Deferred<Response<Exercise>> {
        return apiCalls.updateExerciseAsync(token, classroomId, taskId, exercise)
    }

    fun updateStatusAsync(
        token: String, classroomId: String, taskId: String, exerciseId: String
    ): Deferred<Response<Exercise>> {
        return apiCalls.updateStatusAsync(token, classroomId, taskId, exerciseId)
    }


    fun getFinishedUsersAsync(
        token: String, classroomId: String, exerciseId: String
    ): Deferred<Response<Map<String, String>>> {
        return apiCalls.getFinishedUsersAsync(token, classroomId, exerciseId)
    }

}