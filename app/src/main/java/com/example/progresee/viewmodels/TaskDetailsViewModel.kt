package com.example.progresee.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class TaskDetailsViewModel constructor(
    private val appRepository: AppRepository,
    private val classroomId: String,
    private val taskId: String
) :
    BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val task = MediatorLiveData<Task>()
    fun getTask() = task

    private val exercises = MediatorLiveData<List<Exercise>>()
    fun getExercises() = exercises

    private val _changeExerciseStatus = MutableLiveData<String?>()
    val changeExerciseStatus
        get() = _changeExerciseStatus

    private val _navigateTooTaskFragment = MutableLiveData<Boolean?>()
    val navigateToTaskFragment
        get() = _navigateTooTaskFragment

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar
        get() = _showSnackBar

    init {
        task.addSource(appRepository.getTask(taskId), task::setValue)
        fetchExercisesFromFirebase()
    }

    fun onTaskClicked(exerciseId: String) {
        _changeExerciseStatus.value = exerciseId
    }

    private suspend fun insertExercise(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            appRepository.insertExercise(exercise)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun fetchExercisesFromFirebase() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.getAllExercisesAsync(
                            appRepository.currentToken.value!!,
                            classroomId,taskId
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.insertExercise(it.value)
                            }
                            withContext(Dispatchers.Main){
                                exercises.addSource(appRepository.getExercises(taskId), exercises::setValue)
                            }
                        }
                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }
            hideProgressBar()
        }
    }

    fun deleteTask() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.deleteTaskAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            taskId
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.deleteTaskById(taskId)
                            }
                        }
                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }
            hideProgressBar()
            navigate()
        }

    }

    fun addExercise(description: String) {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.createExerciseAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            taskId, description
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.insertExercise(it.value)
                            }
                            withContext(Dispatchers.Main) {
                                hideProgressBar()
                                showSnackBar()
                            }
                        }

                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }


        }
    }

    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    override fun onDoneNavigating() {
        _navigateTooTaskFragment.value = null
    }

    override fun navigate() {
        _navigateTooTaskFragment.value = true
    }

    fun showSnackBar() {
        _showSnackBar.value=true
    }
    override fun snackBarShown() {
        _showSnackBar.value = null
    }



}