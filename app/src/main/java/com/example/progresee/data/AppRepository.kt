package com.example.progresee.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.*
import com.example.progresee.data.database.AppDatabase
import com.example.progresee.data.network.ApiService
import com.example.progresee.data.network.apicalls.ApiCalls
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import retrofit2.Response

class AppRepository constructor(
    private val dataBase: AppDatabase,
    private val network: ApiService
) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val currentUserEmail = firebaseAuth.currentUser?.email
    fun getCurrentUserEmail() = currentUserEmail

    private var _currentToken = MutableLiveData<String?>()

    private var _isAdmin = MutableLiveData<Boolean?>()
    val isAdmin
        get() = _isAdmin


    val classrooms = MediatorLiveData<List<Classroom>>()

    val currentToken: LiveData<String?>
        get() = _currentToken

    fun setToken(token: String?) {
        _currentToken.value = token
    }

    private val user = MediatorLiveData<User?>()
    fun getUser() = user


    private var _users = dataBase.userDao().getAllUsers()
    val users
        get() = _users


    private val apiCalls: ApiCalls = network.retrofit()


    fun isAdmin(): Boolean {
        _isAdmin.value = true
        return true
    }

    fun notAdmin(): Boolean {
        _isAdmin.value = false
        return false
    }

    fun fetchClassroomsFromDb() {
        classrooms.addSource(dataBase.classroomDao().getClassrooms(), classrooms::setValue)
    }

    fun isUserExist(userId: String): Boolean {
        return dataBase.userDao().isUserExist(userId)
    }

    fun insertUser(user: User) {
        dataBase.userDao().insertUser(user)
    }

    fun insertUsers(data: List<User>?) {
        dataBase.userDao().insertUsers(data)
    }

    fun getUser(userId: String): LiveData<User?> {
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

    fun deleteClassroomById(classroomId: String?) {
        dataBase.classroomDao().deleteClassroomById(classroomId)
    }


    fun getClassroom(classroomId: String): LiveData<Classroom?> {
        return dataBase.classroomDao().getClassroom(classroomId)
    }

    fun insertTask(task: Task) {
        dataBase.taskDao().insert(task)
    }

    fun getTask(taskId: String): LiveData<Task> {
        return dataBase.taskDao().getTask(taskId)
    }

    fun updateTask(task: Task) {
        dataBase.taskDao().updateTask(task)
    }

    fun getAllTasks(classroomId: String): LiveData<List<Task>> {
        return dataBase.taskDao().getTasks(classroomId)
    }

    fun deleteTaskById(taskId: String) {
        dataBase.taskDao().deleteTask(taskId)
    }

    fun getExercises(taskId: String): LiveData<List<Exercise>> {
        return dataBase.exerciseDao().getExercises(taskId)
    }

    fun insertExercise(exercise: Exercise) {
        dataBase.exerciseDao().insertExercise(exercise)
    }

    fun deleteExerciseById(exerciseId: String) {
        dataBase.exerciseDao().deleteExercise(exerciseId)
    }

    fun updateExercise(exercise: Exercise) {
        dataBase.exerciseDao().updateExercise(exercise)
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
    ): Deferred<Response<Map<String, Classroom>>> {
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

    fun clearUsers() {
        dataBase.userDao().clearUsers()
    }

    fun removeUser(userId: String) {
        dataBase.userDao().removeUser(userId)
    }

    fun insertUserFinishedIntoDB(userFinished: FinishedUser) {
        dataBase.userFinishedDao().insertFinishedUser(userFinished)
    }

    fun getUserFinishedFromDB(exerciseId: String): LiveData<List<FinishedUser>> {
        return dataBase.userFinishedDao().getFinishedUser(exerciseId)
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