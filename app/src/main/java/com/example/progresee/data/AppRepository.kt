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
import java.lang.Exception

class AppRepository constructor(
    private val dataBase: AppDatabase,
    private val network: ApiService
) {

    private val _currentToken = MutableLiveData<String?>()
    val currentToken: LiveData<String?>
        get() = _currentToken

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val user = MediatorLiveData<User?>()
    fun getUser() = user

    private val token = MediatorLiveData<String?>()
    fun getToken() = token


    private val _classrooms: LiveData<List<Classroom?>> = dataBase.classroomDao().getClassrooms()
    val classrooms
        get() = _classrooms

    private val _tasks: LiveData<List<Task>> = dataBase.taskDao().getTasks()
    val tasks
        get() = _tasks

    private val _exercises: LiveData<List<Exercise>> = dataBase.exerciseDao().getExercises()
    val exercises
        get() = _exercises

    private val apiCalls: ApiCalls = network.retrofit()


    fun insertClassroom(classroom: Classroom?) {
        dataBase.classroomDao().insert(classroom)
    }

    fun updateClassroom(classroom: Classroom?) {
        dataBase.classroomDao().updateClassroom(classroom)
    }

    fun deleteClassroom(classroom: Classroom?) {
        dataBase.classroomDao().deleteClassroom(classroom)
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

    fun getCurrentUserToken() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.wtf("yay i have a tokenz ${it.result!!.token}")
                _currentToken.value = it.result!!.token

//                currentToken.value = it.result!!.token
//                token.addSource(currentToken, token::setValue)
            }
        }
    }

    fun getCurrentUser(token: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val request = apiCalls.getCurrentUserAsync(token).await()
                    if (request.isSuccessful) {
                        val data = request.body()
                        if (dataBase.userDao().isUserExist(data!!.id)) {
                            withContext(Dispatchers.Main) {
                                user.addSource(dataBase.userDao().getUser(data.id), user::setValue)
                                getCurrentUserToken()
                            }
                        } else {
                            dataBase.userDao().insertUser(data)
                            withContext(Dispatchers.Main) {
                                user.addSource(dataBase.userDao().getUser(data.id), user::setValue)
                                getCurrentUserToken()

                            }
                        }
                    } else {
                        Timber.wtf("${request.code()}${request.errorBody()}")
                    }
                } catch (e: Exception) {
                    Timber.e(e.printStackTrace().toString())
                }
            }
        }
    }

    fun updateUser(token: String?, user: User): Deferred<Response<User>> {
        return apiCalls.updateUser(token, user)
    }

    fun createClassroom(token: String?, name: String): Deferred<Response<Classroom>> {
        return apiCalls.createClassroom(token,name)
    }

    fun addToClassroom(token: String?, userId: Long, classroomId: Long): Deferred<Response<User>> {
        return apiCalls.addToClassroom(token, userId, classroomId)
    }

    fun getUsersInClassroom(token: String?, classroomId: Long): Deferred<Response<List<User>>> {
        return apiCalls.getUsersInClassroom(token, classroomId)
    }

    fun updateClassroom(token: String?, classroom: Classroom): Deferred<Response<Classroom>> {
        return apiCalls.updateClassroom(token, classroom)
    }

    fun leaveClassroom(token: String?, classroomId: Long) : Deferred<Response<User>> {
        return apiCalls.leaveClassRoom(token, classroomId)
    }

    fun removeUser(token: String?, userId: Long, classroomId: Long): Deferred<Response<String>>  {
       return apiCalls.removeUser(token, userId, classroomId)
    }

    fun transferClassroom(token: String?, classroomId: Long, email: String): Deferred<Response<Classroom>>{
        return apiCalls.transferClassroom(token, classroomId, email)
    }


}