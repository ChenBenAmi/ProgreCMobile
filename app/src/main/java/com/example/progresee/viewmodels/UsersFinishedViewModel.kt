package com.example.progresee.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.FinishedUser
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

    private val adapterList = hashMapOf<String, FinishedUser>()
    private val _usersFinished = MutableLiveData<List<FinishedUser>>()
    val usersFinished
        get() = _usersFinished

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
                        val response = appRepository.getFinishedUsersAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            exerciseId
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())

                            data?.forEach {
                                val finishedUser = FinishedUser(it.key, it.value, exerciseId)
                                adapterList[it.key] = finishedUser
                                withContext(Dispatchers.Main) {
                                    _usersFinished.value = adapterList.values.toList()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    } finally {
                        withContext(Dispatchers.Main) {
                            hideProgressBar()
                        }
                    }
                }
            }
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