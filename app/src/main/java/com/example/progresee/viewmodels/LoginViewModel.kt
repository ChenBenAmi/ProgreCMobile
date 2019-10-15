package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class LoginViewModel(private val appRepository: AppRepository) : BaseViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateToClassroomFragment
        get() = _navigateToClassroomFragment

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    fun getCurrentUser(token: String?) {
        uiScope.launch {
            showProgressBar()
            appRepository.setToken(token)
            withContext(Dispatchers.IO) {
                try {
                    val request = appRepository.getCurrentUserAsync(token).await()
                    if (request.isSuccessful) {
                        val data = request.body()
                        if (appRepository.isUserExist(data!!.id)) {
                            withContext(Dispatchers.Main) {
                                appRepository.getUser().addSource(
                                    appRepository.getUser(data.id),
                                    appRepository.getUser()::setValue
                                )
                                withContext(Dispatchers.Main) {
                                    hideProgressBar()
                                    navigate()
                                }
                            }
                        } else {
                            appRepository.insertUser(data)
                            withContext(Dispatchers.Main) {
                                appRepository.getUser().addSource(
                                    appRepository.getUser(data.id),
                                    appRepository.getUser()::setValue
                                )
                                withContext(Dispatchers.Main) {
                                    hideProgressBar()
                                    navigate()
                                }
                            }
                        }
                    } else {
                        Timber.wtf("${request.code()}${request.errorBody()}")
                    }
                } catch (e: Exception) {
                    Timber.e(e.printStackTrace().toString())
                }
                try {
                    val request = appRepository.getClassrooms(token).await()
                    if (request.isSuccessful) {
                        val data = request.body()
                        Timber.wtf("data -------->  $data")
                        if (data != null) {
                            appRepository.insertClassrooms(data)
                            appRepository.loadClassroomsFromDB()
                            Timber.wtf("loaded classes")
                        }
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    override fun navigate() {
        _navigateToClassroomFragment.value = true
    }

    override fun onDoneNavigating() {
        _navigateToClassroomFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}