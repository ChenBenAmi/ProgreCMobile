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

class TaskDetailsViewModel constructor(private val appRepository: AppRepository, private val taskId: Long) :
    ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val task = MediatorLiveData<Task>()
    fun getTask() = task

    val exercises=appRepository.exercises


    init {
        Timber.wtf(taskId.toString())
        task.addSource(appRepository.getTask(taskId), task::setValue)


    }

    private val _changeExerciseStatus = MutableLiveData<Long?>()
    val changeExerciseStatus
        get() = _changeExerciseStatus

    fun onTaskClicked(exerciseId: Long) {
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
                Exercise(98,"a very nice exercise",taskId))
            insertExercise(
                Exercise(97,"a very nice exercise",taskId))
            insertExercise(
                Exercise(96,"a very nice exercise ",taskId))
            insertExercise(
                Exercise(95,"a very nice exercise ",taskId))
            insertExercise(
                Exercise(94,"a very nice exercise ",taskId))
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }



}