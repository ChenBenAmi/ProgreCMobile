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

    private val _navigateToTaskFragment = MutableLiveData<String>()
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
        if (appRepository.currentToken.value == null) {
            currentUser!!.getIdToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.wtf(it.result?.token)
                    uiScope.launch {
                        showProgressBar()
                        appRepository.setToken(it.result?.token)
                        val token = appRepository.currentToken.value
                        Timber.wtf("hey")
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                try {
                                    Timber.wtf("hey1")
                                    val request =
                                        appRepository.getCurrentUserAsync(token).await()
                                    Timber.wtf("hey2")

                                    if (request.isSuccessful) {
                                        Timber.wtf("hey3")

                                        val data = request.body()
                                        if (appRepository.isUserExist(data!!.uid)) {
                                            Timber.wtf("hey4")

                                            withContext(Dispatchers.Main) {

                                                appRepository.getUser().addSource(
                                                    appRepository.getUser(data.uid),
                                                    appRepository.getUser()::setValue
                                                )
                                            }
                                        } else {
                                            Timber.wtf("hey5")

                                            appRepository.insertUser(data)
                                            withContext(Dispatchers.Main) {
                                                appRepository.getUser().addSource(
                                                    appRepository.getUser(data.uid),
                                                    appRepository.getUser()::setValue
                                                )
                                            }
                                        }
                                        Timber.wtf("hey6")

                                        val request2 =
                                            appRepository.getClassroomsAsync(token).await()
                                        Timber.wtf("hey7")

                                        if (request2.isSuccessful) {

                                            val classroomsData = request2.body()
                                            Timber.wtf("data -------->  $classroomsData")
                                            classroomsData?.forEach { classroomEntry ->
                                                appRepository.insertClassroom(classroomEntry.value)
                                            }
                                        } else Timber.wtf(" else 1 ${request2.code()}${request2.errorBody()} ${request2.message()}")
                                    } else Timber.wtf("else 2 ${request.code()}${request.errorBody()}")
                                } catch (e: Exception) {
                                    Timber.e("${e.printStackTrace()}${e.message}")
                                }
                            }
                        }
                        appRepository.fetchClassroomsFromDb()
                        hideProgressBar()
                    }
                }
            }
        }
    }

    fun onClassroomClicked(uid: String) {
        _navigateToTaskFragment.value = uid
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
        _showProgressBar.value = false
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        uiScope.cancel()
    }


}