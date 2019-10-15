package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.progresee.data.AppRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class ClassroomViewModel constructor(
    private val appRepository: AppRepository
) :
    BaseViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val classrooms = appRepository.classrooms
    val user = appRepository.getUser()

    private val _navigateToTaskFragment = MutableLiveData<Long>()
    val navigateToTaskFragment
        get() = _navigateToTaskFragment

    private val _navigateToCreateClassroomFragment = MutableLiveData<Boolean?>()
    val navigateToCreateClassroomFragment
        get() = _navigateToCreateClassroomFragment

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar


    fun getCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        currentUser!!.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.wtf(it.result?.token)
                uiScope.launch {
                    showProgressBar()
                    appRepository.setToken(it.result?.token)
                    withContext(Dispatchers.IO) {
                        try {
                            val request =
                                appRepository.getCurrentUserAsync(it.result?.token).await()
                            if (request.isSuccessful) {
                                val data = request.body()
                                if (appRepository.isUserExist(data!!.id)) {
                                    withContext(Dispatchers.Main) {
                                        appRepository.getUser().addSource(
                                            appRepository.getUser(data.id),
                                            appRepository.getUser()::setValue
                                        )
                                        hideProgressBar()
                                    }
                                } else {
                                    appRepository.insertUser(data)
                                    withContext(Dispatchers.Main) {
                                        appRepository.getUser().addSource(
                                            appRepository.getUser(data.id),
                                            appRepository.getUser()::setValue
                                        )
                                        hideProgressBar()

                                    }
                                }
                            } else {
                                Timber.wtf("${request.code()}${request.errorBody()}")
                            }
                        } catch (e: Exception) {
                            Timber.e(e.printStackTrace().toString())
                        }
                        try {
                            val request = appRepository.getClassrooms(it.result?.token).await()
                            if (request.isSuccessful) {
                                val data = request.body()
                                Timber.wtf("data -------->  $data")
                                if (data != null) {
                                    appRepository.insertClassrooms(data)
                                }
                            } else {
                            }
                        } catch (e: Exception) {
                            Timber.wtf("${e.message}${e.printStackTrace()}")
                        }
                    }
                }
            }
        }
    }

    fun onClassroomClicked(id: Long) {
        _navigateToTaskFragment.value = id
    }

    fun doneNavigateToTaskFragment() {
        _navigateToTaskFragment.value = null
    }

    fun navigateToCreateClassroomFragment() {
        _navigateToCreateClassroomFragment.value = true
    }

    fun doneNavigateToCreateClassroomFragment() {
        _navigateToCreateClassroomFragment.value = null
    }

    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}