package com.app.progrec.viewmodels

import androidx.lifecycle.MutableLiveData
import com.app.progrec.data.AppRepository
import kotlinx.coroutines.*

class LoginViewModel(private val appRepository: AppRepository) : BaseViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateToClassroomFragment
        get() = _navigateToClassroomFragment


    override fun navigate() {
        _navigateToClassroomFragment.value = true
    }

    override fun onDoneNavigating() {
        _navigateToClassroomFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}