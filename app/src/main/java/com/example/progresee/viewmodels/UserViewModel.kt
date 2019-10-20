package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber

class UserViewModel(private val appRepository: AppRepository, private val classroomId: String) :
    BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val users = appRepository.users

    private val _navigateBackToTaskFragment = MutableLiveData<Boolean?>()
    val navigateBackToTaskFragment
        get() = _navigateBackToTaskFragment


    init {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        Timber.wtf(classroomId)
                        val response = appRepository.getUsersInClassroomAsync(
                            appRepository.currentToken.value!!,
                            classroomId
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.forEach {
                                appRepository.insertUser(it.value)
                            }
                            withContext(Dispatchers.Main) {
                                hideProgressBar()
                            }
                        } else {
                            Timber.wtf("Something went wrong ${response.code()} ${response.errorBody().toString()}")
                        }
                    } catch (e: Exception) {
                        Timber.wtf("${e.message}${e.printStackTrace()}")
                    }
                }
            }
        }
    }

    fun onUserClicked(userId: String) {

    }

    override fun navigate() {
        super.navigate()
    }

    override fun onDoneNavigating() {
        super.onDoneNavigating()
    }

    override fun showProgressBar() {
        super.showProgressBar()
    }

    override fun hideProgressBar() {
        super.hideProgressBar()
    }


}