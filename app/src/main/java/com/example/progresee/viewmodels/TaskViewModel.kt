package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class TaskViewModel(private val appRepository: AppRepository, private val classroomId: String) :
    BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _tasks: LiveData<List<Task>> = appRepository.getAllTasks(classroomId)
    val tasks
        get() = _tasks

    private val classroom = MediatorLiveData<Classroom>()
    fun getClassroom() = classroom

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar
        get() = _showSnackBar


    private val _navigateTooTaskDetailsFragment = MutableLiveData<String>()
    val navigateToTaskDetailsFragment
        get() = _navigateTooTaskDetailsFragment

    private val _navigateToCreateTask = MutableLiveData<Boolean?>()
    val navigateToCreateTask
        get() = _navigateToCreateTask

    private val _navigateBackToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateBackToClassroomFragment
        get() = _navigateBackToClassroomFragment

    private val _checkOwnerShip = MutableLiveData<Boolean?>()
    val checkOwnerShip
        get() = _checkOwnerShip

    init {
        uiScope.launch {
            fetchTasksFromFirebase()
            classroom.addSource(appRepository.getClassroom(classroomId), classroom::setValue)
        }
    }

    private fun fetchTasksFromFirebase() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.getAllTasksAsync(
                            appRepository.currentToken.value!!,
                            classroomId
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.insertTask(it.value)
                            }
                        }
                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }
            hideProgressBar()
        }
    }

    fun deleteClassRoom() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.deleteClassroomAsync(
                            appRepository.currentToken.value!!,
                            classroom.value!!.uid
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.deleteClassroomById(it.value)
                            }
                        }
                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }
            hideProgressBar()
            onClassroomDeleted()
        }

    }

    fun checkClassroomOwnerShip(classroom: Classroom?) {
        val user = appRepository.getUser().value
        user?.let {
            classroom?.let {
                if (user.email == classroom.owner)
                    _checkOwnerShip.value = true
            }
        }
    }

    fun addToClassRoom(text: String) {
        Timber.wtf(text)
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.addToClassroomAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            text
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data != null) {
                                Timber.wtf(data.toString())
                                data.forEach {
                                    appRepository.insertClassroom(it.value)
                                }
                                withContext(Dispatchers.Main) {
                                    hideProgressBar()
                                    showSnackBar()
                                }
                            }
                        } else {
                            Timber.wtf("${response.code()}${response.errorBody()}")
                        }
                    } catch (e: Exception) {
                        Timber.wtf("${e.message}${e.printStackTrace()}")
                    }
                }
            }
        }
    }


    fun checkedClassroomOwnerShip() {
        _checkOwnerShip.value = null
    }


    fun onTaskClicked(id: String) {
        _navigateTooTaskDetailsFragment.value = id
    }

    fun doneNavigateToTaskDetailsFragment() {
        _navigateTooTaskDetailsFragment.value = null
    }

    private fun onClassroomDeleted() {
        _navigateBackToClassroomFragment.value = true
    }

    fun doneNavigateToClassroomFragment() {
        _navigateBackToClassroomFragment.value = null
    }

    fun navigateToCreateTaskFragment() {
        _navigateToCreateTask.value = true
    }

    fun doneNavigationToCreateTaskFragment() {
        _navigateToCreateTask.value = null
    }

    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    fun showSnackBar() {
        _showSnackBar.value = true
    }

    override fun snackBarShown() {
        _showSnackBar.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        uiScope.cancel()
    }


}