package com.example.progresee.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Task
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class TaskViewModel(private val appRepository: AppRepository, classroomId: Long) :
    BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val classroom = MediatorLiveData<Classroom>()
    fun getClassroom() = classroom

    val tasks = appRepository.tasks

    init {
        Timber.wtf(classroomId.toString())
        uiScope.launch {
            classroom.addSource(appRepository.getClassroom(classroomId), classroom::setValue)
        }

    }

    private val _navigateTooTaskDetailsFragment = MutableLiveData<Long>()
    val navigateToTaskDetailsFragment
        get() = _navigateTooTaskDetailsFragment

    private val _navigateBackToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateBackToClassroomFragment
        get() = _navigateBackToClassroomFragment



    fun onTaskClicked(id: Long) {
        _navigateTooTaskDetailsFragment.value = id
    }

    fun doneNavigateToTaskDetailsFragment() {
        _navigateTooTaskDetailsFragment.value = null
    }

    private fun onClassroomDeleted() {
        _navigateBackToClassroomFragment.value=true
    }
    fun doneNavigateToClassroomFragment() {
        _navigateBackToClassroomFragment.value=null
    }

    private suspend fun insertTask(task: Task) {
        withContext(Dispatchers.IO) {
            appRepository.insertTask(task)
        }
    }

    fun deleteClassRoom() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                appRepository.deleteClassroom(getClassroom().value)

            }
            onClassroomDeleted()
        }
    }

    fun insertDummyData() {
        uiScope.launch {
            insertTask(
                Task(
                    100,
                    "java",
                    "ZrrcYvrGgxakww8qHeDWdN3YC1OOEQimJd7zlObnCDkdwtpU3XjniOqGGU4fT91quvOtbzjIH9r7SuMbB0NgdKZ6FBHEzLGBp7X52gefZ7TS973leFJbUsmVXnGVZ8nYExsu27iQdnxLjsN2wDBhmIGrmfHP8T4jyweZ8wvI0V0EAcYnrRPmiFBltWcdMSZ9osdRCDGM0Ew8xX4PT5TmFW5Fvm0GfnOigcYL0mK2mjmqWflp0CNQHK9hJgeM7Bs",
                    "https://i.imgur.com/V9mmwJN.jpg",
                    Calendar.getInstance().time,
                    Calendar.getInstance().time
                )
            )
            Timber.wtf(Calendar.getInstance().time.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}