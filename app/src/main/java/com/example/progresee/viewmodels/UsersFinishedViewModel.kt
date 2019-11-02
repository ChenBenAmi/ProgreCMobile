package com.example.progresee.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.UserFinished
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber

class UsersFinishedViewModel(
    private val appRepository: AppRepository,
    private val classroomId: String,
    private val exerciseId: String
) : BaseViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _navigateBackToTaskFragment = MutableLiveData<Boolean?>()
    val navigateBackToTaskFragment
        get() = _navigateBackToTaskFragment

    private val usersFinished = MediatorLiveData<List<UserFinished>>()
    fun getUsersFinishedVariable() = usersFinished

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar


    init {
        getUsersFinished()
    }

    private fun getUsersFinished() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        Timber.wtf("hey")
                        val response = appRepository.getFinishedUsersAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            exerciseId
                        ).await()
                        Timber.wtf("hey1")
                        if (response.isSuccessful) {
                            Timber.wtf("hey2")
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.insertUserFinishedIntoDB(it.value)
                                Timber.wtf("hey3")
                                withContext(Dispatchers.Main) {
                                    usersFinished.addSource(
                                        appRepository.getUserFinishedFromDB(exerciseId),
                                        usersFinished::setValue
                                    )
                                }
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


    override fun navigate() {
        super.navigate()
    }

    override fun onDoneNavigating() {
        super.onDoneNavigating()
    }

    override fun showProgressBar() {
        super.showProgressBar()
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        super.hideProgressBar()
        _showProgressBar.value = null
    }


}