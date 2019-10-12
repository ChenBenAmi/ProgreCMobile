package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.progresee.data.AppRepository
import com.example.progresee.views.ClassroomFragmentDirections
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

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

    fun doneNavigateToTaskFragment() {
        _navigateToTaskFragment.value = null
    }

    fun navigateToCreateClassroomFragment() {
        _navigateToCreateClassroomFragment.value = true
    }

    fun doneNavigateToCreateClassroomFragment() {
        _navigateToCreateClassroomFragment.value = null
    }

    fun getCurrentUser(token:String) {
        appRepository.getCurrentUser(token)
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}