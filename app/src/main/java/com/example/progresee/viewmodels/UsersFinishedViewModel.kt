package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
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
                                if (it.isNotEmpty()) {
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
                                } else {
                                    withContext(Dispatchers.Main) {
                                        _isEmpty.value = true
                                        Timber.wtf("no users available ${response.code()}")
                                    }
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

    fun showSnackBarRefresh() {
        _showSnackBarRefresh.value = true
    }

    fun hideRefreshSnackBar() {
        _showSnackBarRefresh.value = null
    }


}