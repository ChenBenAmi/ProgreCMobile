package com.app.progrec.viewmodels

import androidx.lifecycle.MutableLiveData
import com.app.progrec.beans.FinishedUser
import com.app.progrec.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber

class UsersFinishedViewModel(
    private val appRepository: AppRepository,
    private val classroomId: String,
    private val exerciseId: String
) : BaseViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private var _isEmpty = MutableLiveData<Boolean?>()
    val isEmpty
        get() = _isEmpty

    private val adapterList = hashMapOf<String, FinishedUser>()
    private val _usersFinished = MutableLiveData<List<FinishedUser>>()
    val usersFinished
        get() = _usersFinished

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBarRefresh = MutableLiveData<Boolean?>()
    val showSnackBarRefresh
        get() = _showSnackBarRefresh

    private val _showSnackBarHttpError = MutableLiveData<Int?>()
    val showSnackBarHttpError
        get() = _showSnackBarHttpError

    init {
        getUsersFinished()
    }

    fun getUsersFinished() {
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
                            data?.let {
                                    withContext(Dispatchers.Main) {
                                        _isEmpty.value = false
                                    }
                                    data.forEach { user ->
                                        val finishedUser =
                                            FinishedUser(user.key, user.value, exerciseId)
                                        adapterList[user.key] = finishedUser
                                        withContext(Dispatchers.Main) {
                                            _usersFinished.value = adapterList.values.toList()
                                        }
                                    }
                                }
                            } else {
                            withContext(Dispatchers.Main) {
                                showHttpErrorSnackBar400()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            showHttpErrorSnackBarNetwork()
                        }
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


    override fun showProgressBar() {
        super.showProgressBar()
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        super.hideProgressBar()
        _showProgressBar.value = null
    }

    fun showSnackBarRefresh() {
        _showSnackBarRefresh.value = true
    }

    fun hideRefreshSnackBar() {
        _showSnackBarRefresh.value = null
    }

    override fun showHttpErrorSnackBar400() {
        _showSnackBarHttpError.value = 1
    }

    override fun showHttpErrorSnackBarNetwork() {
        _showSnackBarHttpError.value = 2
    }

    override fun hideHttpErrorSnackBar() {
        _showSnackBarHttpError.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        uiScope.cancel()
    }


}