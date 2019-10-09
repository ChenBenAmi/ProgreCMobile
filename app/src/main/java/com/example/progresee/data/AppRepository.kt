package com.example.progresee.data

import androidx.lifecycle.LiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.data.database.AppDatabase
import com.example.progresee.data.network.ApiService
import com.example.progresee.data.network.apicalls.ApiCalls
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class AppRepository constructor(
    private val dataBase: AppDatabase,
    private val network: ApiService
) {

    private val _classrooms: LiveData<List<Classroom>> = dataBase.classroomDao().getClassrooms()
    val classrooms
        get() = _classrooms

    private val _tasks: LiveData<List<Task>> = dataBase.taskDao().getTasks()
    val tasks
        get() = _tasks

    private val _exercises: LiveData<List<Exercise>> = dataBase.exerciseDao().getExercises()
    val exercises
        get() = _exercises

    private val apiCalls: ApiCalls = network.retrofit()


    fun insertClassroom(classroom: Classroom) {
        dataBase.classroomDao().insert(classroom)
    }

    fun updateClassroom(classroom: Classroom) {
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


    fun loginWithGoogle() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.IO) {
                try {
                    val request = apiCalls.login()
                    val response = request.await()
                    if (response.isSuccessful) {
                        val data = response.body()
                        Timber.i(data.toString())
                    } else {
                        Timber.e(response.code().toString())
                    }
                } catch (e: Exception) {
                    Timber.e(e.printStackTrace().toString())
                }
            }
        }
    }


}