package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CreateTaskViewModel(
    private val appRepository: AppRepository, classroomId: String, taskId: String?
) : BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _navigateBackToTaskFragment=MutableLiveData<Boolean?>()
    val navigateBackToTaskFragment
    get() = _navigateBackToTaskFragment


    private val _stringLength = MutableLiveData<Int?>()
    val stringLength: LiveData<Int?>
        get() = _stringLength

    private val _descriptionStringLength = MutableLiveData<Int?>()
    val descriptionStringLength: LiveData<Int?>
        get() = _descriptionStringLength



    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    override fun navigate() {
        _navigateBackToTaskFragment.value=true
    }

    override fun onDoneNavigating() {
        _navigateBackToTaskFragment.value = null
    }

    override fun snackBarShown() {
        _stringLength.value = null
        _descriptionStringLength.value=null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}