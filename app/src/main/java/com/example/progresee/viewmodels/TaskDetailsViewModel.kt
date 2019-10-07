package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.data.AppRepository

class TaskDetailsViewModel constructor(private val appRepository: AppRepository,private val taskId:Long) : ViewModel() {


    private val _changeStatus = MutableLiveData<Boolean?>()
    val changeStatus
        get() = _changeStatus

    fun onTaskClicked(status: Boolean) {
        _changeStatus.value = status
    }

}