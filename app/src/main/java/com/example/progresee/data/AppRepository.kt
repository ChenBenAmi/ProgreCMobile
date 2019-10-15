package com.example.progresee.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.beans.User
import com.example.progresee.data.database.AppDatabase
import com.example.progresee.data.network.ApiService
import com.example.progresee.data.network.apicalls.ApiCalls
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber

class AppRepository constructor(
    private val dataBase: AppDatabase,
    private val network: ApiService
) {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private var _currentToken = MutableLiveData<String?>()

    val currentToken: LiveData<String?>
        get() = _currentToken

    fun setToken(token: String?) {
        _currentToken.value = token
    }

    private val user = MediatorLiveData<User?>()
    fun getUser() = user


    private var _classrooms = dataBase.classroomDao().getClassrooms()
    val classrooms
        get() = _classrooms

    private var _users = dataBase.userDao().getAllUsers()
    val users
        get() = _users

    private val _tasks: LiveData<List<Task>> = dataBase.taskDao().getTasks()
    val tasks
        get() = _tasks

    private val _exercises: LiveData<List<Exercise>> = dataBase.exerciseDao().getExercises()
    val exercises
        get() = _exercises

    private val apiCalls: ApiCalls = network.retrofit()


    fun fetchClassroomsFromDb() {
        _classrooms=dataBase.classroomDao().getClassrooms()
        Timber.wtf(dataBase.classroomDao().getClassrooms().toString())
    }
    fun isUserExist(userId: Long): Boolean {
        return dataBase.userDao().isUserExist(userId)
    }

    fun insertUser(user: User) {
        dataBase.userDao().insertUser(user)
    }

    fun insertUsers(data: List<User>?) {
        dataBase.userDao().insertUsers(data)
    }

    fun getUser(userId: Long): LiveData<User?> {
        return dataBase.userDao().getUser(userId)
    }

    fun insertClassroom(classroom: Classroom?) {
        dataBase.classroomDao().insert(classroom)
    }

    fun updateClassroom(classroom: Classroom?) {
        dataBase.classroomDao().updateClassroom(classroom)
    }

    fun deleteClassroom(classroom: Classroom?) {
        dataBase.classroomDao().deleteClassroom(classroom)
    }

    fun deleteClassroomById(classroomId: Long?) {
        dataBase.classroomDao().deleteClassroomById(classroomId)
    }

    fun getClassroom(classroomId: Long): LiveData<Classroom?> {
        return dataBase.classroomDao().getClassroom(classroomId)
    }

    fun insertTask(task: Task) {
        dataBase.taskDao().insert(task)
    }

    fun getTask(taskId: Long): LiveData<Task> {
        return dataBase.taskDao().getTask(taskId)
    }

    fun insertExercise(exercise: Exercise) {
        dataBase.exerciseDao().insertExercise(exercise)
    }

    fun getCurrentUserAsync(token: String?): Deferred<Response<User>> {
        return apiCalls.getCurrentUserAsync(token)

    }

    fun updateUser(token: String?, user: User): Deferred<Response<User>> {
        return apiCalls.updateUserAsync(token, user)
    }

    fun createClassroomAsync(token: String?, name: String): Deferred<Response<Classroom>> {
        return apiCalls.createClassroomAsync(token, name)
    }

    fun deleteClassroomAsync(token: String?, classroomId: Long): Deferred<Response<Long>> {
        return apiCalls.deleteClassroomAsync(token, classroomId)
    }

    fun addToClassroomAsync(
        token: String?,
        userEmail: String,
        classroomId: Long
    ): Deferred<Response<Long>> {
        return apiCalls.addToClassroomAsync(token, userEmail, classroomId)
    }

    fun getUsersInClassroomAsync(token: String?, classroomId: Long): Deferred<Response<List<User>>> {
        return apiCalls.getUsersInClassroomAsync(token, classroomId)
    }

    fun updateClassroomAsync(token: String?, classroom: Classroom): Deferred<Response<Classroom>> {
        return apiCalls.updateClassroomAsync(token, classroom)
    }

    fun leaveClassroom(token: String?, classroomId: Long): Deferred<Response<User>> {
        return apiCalls.leaveClassRoomAsync(token, classroomId)
    }

    fun removeUser(token: String?, userId: Long, classroomId: Long): Deferred<Response<String>> {
        return apiCalls.removeUserAsync(token, userId, classroomId)
    }

    fun transferClassroom(
        token: String?,
        classroomId: Long,
        email: String
    ): Deferred<Response<Classroom>> {
        return apiCalls.transferClassroomAsync(token, classroomId, email)
    }

    fun getClassroom(token: String?, classroomId: Long): Deferred<Response<Classroom>> {
        return apiCalls.getClassroom(token, classroomId)
    }

    fun getClassroomsAsync(token: String?): Deferred<Response<List<Classroom>>> {
        return apiCalls.getClassrooms(token)
    }

    fun insertClassrooms(data: List<Classroom>) {
        dataBase.classroomDao().insertAll(data)
    }


}