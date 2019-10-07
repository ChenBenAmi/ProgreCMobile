package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.progresee.beans.Classroom
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber

class TaskViewModel(private val appRepository: AppRepository, private val classroomId: Long) :
    ViewModel() {

    private val classroom = MediatorLiveData<Classroom>()

    fun getClassroom() = classroom

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    init {
        Timber.wtf(classroomId.toString())
        classroom.addSource(appRepository.getClassroom(classroomId), classroom::setValue)

    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}