package com.example.progresee.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class TaskDetailsViewModel constructor(private val appRepository: AppRepository, private val taskId: String) :
    BaseViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val task = MediatorLiveData<Task>()
    fun getTask() = task

    private val _changeExerciseStatus = MutableLiveData<String?>()
    val changeExerciseStatus
        get() = _changeExerciseStatus

    val exercises=appRepository.exercises

    init {
        Timber.wtf(taskId.toString())
        task.addSource(appRepository.getTask(taskId), task::setValue)
    }

    fun onTaskClicked(exerciseId: String) {
        _changeExerciseStatus.value = exerciseId
    }
    private suspend fun insertExercise(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            appRepository.insertExercise(exercise)
        }
    }
    fun insertDummyData() {
        uiScope.launch {
            insertExercise(
                Exercise(taskId,"a very nice exercise",Calendar.getInstance().time, listOf("s","S")))
            insertExercise(
                Exercise(taskId,"a very nice exercise",Calendar.getInstance().time, listOf("s","S")))
            insertExercise(
                Exercise(taskId,"a very nice exercise ",Calendar.getInstance().time, listOf("s","S")))
            insertExercise(
                Exercise(taskId,"a very nice exercise ",Calendar.getInstance().time, listOf("s","S")))
            insertExercise(
                Exercise(taskId,"a very nice exercise ",Calendar.getInstance().time, listOf("s","S")))
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}