package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.beans.Classroom
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class ClassroomViewModel constructor(
    private val appRepository: AppRepository
) :
    ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val classrooms = appRepository.classrooms

    private val _navigateToTaskFragment = MutableLiveData<Long>()
    val navigateToTaskFragment
        get() = _navigateToTaskFragment

    private val _navigateToCreateClassroomFragment = MutableLiveData<Boolean?>()
    val navigateToCreateClassroomFragment
        get() = _navigateToCreateClassroomFragment

    fun onClassroomClicked(id: Long) {
        _navigateToTaskFragment.value = id
    }

    suspend fun insertClassroom(classroom: Classroom) {
        withContext(Dispatchers.IO) {
            appRepository.insertClassroom(classroom)
        }
    }

    fun insertDummyData() {
        uiScope.launch {
            insertClassroom(Classroom(1, "java110", "chen", Calendar.getInstance().time, 5))
            Timber.wtf(Calendar.getInstance().time.toString())
        }
    }

    fun navigateToCreateClassroomFragment() {
        _navigateToCreateClassroomFragment.value = true
    }

    fun doneNavigateToCreateClassroomFragment() {
        _navigateToCreateClassroomFragment.value = null
    }

    fun doneNavigateToTaskFragment() {
        _navigateToTaskFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}