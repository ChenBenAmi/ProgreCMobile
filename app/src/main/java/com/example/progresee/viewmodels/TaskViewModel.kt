package com.example.progresee.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.Task
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class TaskViewModel(private val appRepository: AppRepository, private val classroomId: String) :
    BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val classroom = MediatorLiveData<Classroom>()
    fun getClassroom() = classroom

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar
        get() = _showSnackBar

    val tasks = appRepository.tasks

    private val _navigateTooTaskDetailsFragment = MutableLiveData<String>()
    val navigateToTaskDetailsFragment
        get() = _navigateTooTaskDetailsFragment

    private val _navigateBackToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateBackToClassroomFragment
        get() = _navigateBackToClassroomFragment

    private val _navigateToClassroomUsersFragment = MutableLiveData<Boolean?>()
    val navigateToClassroomUsersFragment
        get() = _navigateToClassroomUsersFragment

    private val _checkOwnerShip = MutableLiveData<Boolean?>()
    val checkOwnerShip
        get() = _checkOwnerShip

    init {
        uiScope.launch {
            classroom.addSource(appRepository.getClassroom(classroomId), classroom::setValue)
        }
    }

    fun deleteClassRoom() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                try {
                    val response = appRepository.deleteClassroomAsync(
                        appRepository.currentToken.value,
                        classroom.value!!.uid
                    ).await()
                    if (response.isSuccessful) {
                        val data = response.body()
                        Timber.wtf(data.toString())
                        appRepository.deleteClassroomById(data)
                    }
                } catch (e: Exception) {
                    Timber.wtf("Something went wrong")
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
                try {
                    Timber.wtf("hey")
                    val response = appRepository.addToClassroomAsync(
                        appRepository.currentToken.value,
                        text,
                        classroomId
                    ).await()
                    Timber.wtf("${appRepository.currentToken.value} $text $classroomId")
                    Timber.wtf("hey1")
                    if (response.isSuccessful) {
                        Timber.wtf("hey2")
                        val data = response.body()
                        if (data != null)
                            withContext(Dispatchers.Main) {
                                Timber.wtf("hey3")
                                hideProgressBar()
                                _showSnackBar.value = true
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

    fun navigateToClassroomUsersPage() {
        navigateToClassroomUsersFragment.value = true
    }

    fun navigateToClassroomUsersPageDone() {
        navigateToClassroomUsersFragment.value = null
    }


    private suspend fun insertTask(task: Task) {
        withContext(Dispatchers.IO) {
            appRepository.insertTask(task)
        }
    }

    fun insertDummyData() {
        uiScope.launch {
            insertTask(
                Task(
                    UUID.randomUUID().toString(),
                    "java",
                    "ZrrcYvrGgxakww8qHeDWdN3YC1OOEQimJd7zlObnCDkdwtpU3XjniOqGGU4fT91quvOtbzjIH9r7SuMbB0NgdKZ6FBHEzLGBp7X52gefZ7TS973leFJbUsmVXnGVZ8nYExsu27iQdnxLjsN2wDBhmIGrmfHP8T4jyweZ8wvI0V0EAcYnrRPmiFBltWcdMSZ9osdRCDGM0Ew8xX4PT5TmFW5Fvm0GfnOigcYL0mK2mjmqWflp0CNQHK9hJgeM7Bs",
                    listOf("https://i.imgur.com/V9mmwJN.jpg"),
                    listOf("https://wwww.google.com"),
                    Calendar.getInstance().time,
                    Calendar.getInstance().time
                )
            )
            Timber.wtf(Calendar.getInstance().time.toString())
        }
    }

    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    override fun snackBarShown() {
        _showSnackBar.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}