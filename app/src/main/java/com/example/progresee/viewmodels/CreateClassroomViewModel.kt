package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.beans.Classroom
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import java.util.*

class CreateClassroomViewModel(private val appRepository: AppRepository) : ViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateBackToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateBackToClassroomFragment: LiveData<Boolean?>
        get() = _navigateBackToClassroomFragment

    private val _stringLength = MutableLiveData<Boolean?>()
    val stringLength: LiveData<Boolean?>
        get() = _stringLength


    fun onSavePressed(name: String) {
        if (name.length > 60) {
            _stringLength.value = true
        } else {
            uiScope.launch {
                //TODO change to network logic
                insertClassroom(Classroom(0, name, "chen", Calendar.getInstance().time, 0))
                _navigateBackToClassroomFragment.value = true
            }
        }
    }

    fun snackbarShown() {
        _stringLength.value = null
    }

    fun onDoneNavigating() {
        _navigateBackToClassroomFragment.value = null
    }

    suspend fun insertClassroom(classroom: Classroom) {
        withContext(Dispatchers.IO) {
            appRepository.insertClassroom(classroom)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}