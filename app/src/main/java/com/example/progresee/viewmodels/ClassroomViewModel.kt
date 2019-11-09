package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
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

    private var _isAdmin = MutableLiveData<Boolean?>()
    val isAdmin
        get() = _isAdmin

    private val adapterList = hashMapOf<String, Classroom>()
    val classrooms = MutableLiveData<List<Classroom>>()

    private var _isEmpty = MutableLiveData<Boolean?>()
    val isEmpty
        get() = _isEmpty


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


    init {
        getCurrentUser()
    }

    fun fetchClassrooms() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val request =
                        appRepository.getClassroomsAsync(appRepository.currentToken.value!!).await()
                    if (request.isSuccessful) {
                        val classroomsData = request.body()
                        Timber.wtf("data -------->  $classroomsData")
                        classroomsData?.forEach { classroomEntry ->
                            setListeners(classroomEntry.key)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            _isEmpty.value = true
                            Timber.wtf("else 2 ${request.code()}${request.errorBody()}")

                        }
                    }
                } catch (e: Exception) {
                    Timber.wtf("${e.message}${e.message}${e.stackTrace}")
                }

            }
        }

    }

    private fun getCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        if (appRepository.currentToken.value == null) {
            if (currentUser?.email.equals("hedsean@gmail.com")) {
                _isAdmin.value = appRepository.isAdmin()
            } else {
                _isAdmin.value = appRepository.notAdmin()
            }
            currentUser!!.getIdToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.wtf(it.result?.token)
                    uiScope.launch {
                        showProgressBar()
                        appRepository.setToken(it.result?.token!!)
                        val token = appRepository.currentToken.value
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                try {
                                    val request =
                                        appRepository.getCurrentUserAsync(token).await()
                                    if (request.isSuccessful) {
                                        val data = request.body()
                                        data?.let { user ->
                                            withContext(Dispatchers.Main) {
                                                appRepository.setCurrentUser(user)
                                            }
                                            fetchClassrooms()
                                        }
                                    }
                                } catch (e: Exception) {
                                    Timber.e(" catch clause -> ${e.printStackTrace()}${e.message}")
                                } finally {
                                    withContext(Dispatchers.Main) {
                                        hideProgressBar()
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } else {
            fetchClassrooms()
        }
    }

    private fun setListeners(uid: String) {
        val db = appRepository.getFirestoreDB()
        val docRef = db.collection("classrooms")
            .document(uid)

        docRef.addSnapshotListener { snapshot, e ->

            if (e != null) {
                Timber.wtf("Listen failed $e")
            }
            if (snapshot != null && snapshot.exists()) {
                Timber.wtf("Current data: ${snapshot.data}")

                val classroomFirestore =
                    snapshot.toObject(Classroom::class.java)
                Timber.wtf("classroom -> $classroomFirestore")
                classroomFirestore?.let {
                    Timber.wtf("formatted classroom is -> $it")
                    adapterList[it.uid] = it
                    classrooms.value = adapterList.values.toList()

                }
            } else {
                Timber.wtf("Current data: null")
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