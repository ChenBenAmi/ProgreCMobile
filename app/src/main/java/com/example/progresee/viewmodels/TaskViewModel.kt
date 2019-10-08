package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Task
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class TaskViewModel(private val appRepository: AppRepository, private val classroomId: Long) :
    ViewModel() {

    private val classroom = MediatorLiveData<Classroom>()
    fun getClassroomName() = classroom

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val tasks = appRepository.tasks

    init {
        Timber.wtf(classroomId.toString())
        classroom.addSource(appRepository.getClassroom(classroomId), classroom::setValue)

    }

    private suspend fun insertTask(task: Task) {
        withContext(Dispatchers.IO) {
            appRepository.insertTask(task)
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
    private val _navigateTooTaskDetailsFragment = MutableLiveData<Long>()
    val navigateToTaskDetailsFragment
        get() = _navigateTooTaskDetailsFragment

    fun onTaskClicked(id: Long) {
        _navigateTooTaskDetailsFragment.value = id
    }

    fun doneNavigateToTaskDetailsFragment() {
        _navigateTooTaskDetailsFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}