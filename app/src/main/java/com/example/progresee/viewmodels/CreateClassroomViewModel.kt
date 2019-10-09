package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.beans.Classroom
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class CreateClassroomViewModel(
    private val appRepository: AppRepository,
    classroomId: Long
) : ViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val classroom = MediatorLiveData<Classroom>()
    fun getClassroom() = classroom

    private val _navigateBackToClassroomFragment = MutableLiveData<Long?>()
    val navigateBackToClassroomFragment: LiveData<Long?>
        get() = _navigateBackToClassroomFragment

    private val _stringLength = MutableLiveData<Int?>()
    val stringLength: LiveData<Int?>
        get() = _stringLength

    init {
        if (classroomId > 0) {
            classroom.addSource(appRepository.getClassroom(classroomId), classroom::setValue)
        }
    }

    fun onSavePressed(name: String) {
        if (name.length > 60) {
            _stringLength.value = 1
        } else if(name.isEmpty()) {
            _stringLength.value = 2
        } else {
            uiScope.launch {
                //TODO change to network logic
                if (classroom.value == null) {
                    insertClassroom(Classroom(0, name, "chen", Calendar.getInstance().time, 0))
                    _navigateBackToClassroomFragment.value = 0
                } else {
                    updateClassroom(name)
                    _navigateBackToClassroomFragment.value = 0
                }

            }
        }
    }

    fun snackBarShown() {
        _stringLength.value = null
    }

    fun onDoneNavigating() {
        _navigateBackToClassroomFragment.value = null
    }

    private suspend fun insertClassroom(classroom: Classroom) {
        withContext(Dispatchers.IO) {
            appRepository.insertClassroom(classroom)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    private fun updateClassroom(name: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (classroom.value != null) {
                    appRepository.updateClassroom(
                        Classroom(
                            classroom.value!!.id,
                            name,
                            "hey1",
                            Calendar.getInstance().time,
                            0
                        )
                    )
                }
            }
        }
    }


}