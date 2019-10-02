package com.example.progresee.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import java.time.LocalDateTime

class ClassroomViewModel constructor(application: Application,  val appRepository: AppRepository) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val classrooms = appRepository.classrooms

    private val _navigateToTaskFragment = MutableLiveData<Long>()
    val navigateToTaskFragment
        get() = _navigateToTaskFragment

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
            insertClassroom(Classroom(1, "java", "me", LocalDateTime.now(),5))
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}