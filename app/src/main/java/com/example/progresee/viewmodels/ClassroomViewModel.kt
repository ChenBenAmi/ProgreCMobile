package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Classroom
import com.example.progresee.beans.ClassroomFirestore
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

    private fun getCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        if (appRepository.currentToken.value == null) {
            if (currentUser?.email.equals("chasdajsdas")) {
                _isAdmin.value = appRepository.isAdmin()
            } else {
                _isAdmin.value = appRepository.notAdmin()
            }
            currentUser!!.getIdToken(true).addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.wtf(it.result?.token)
                    uiScope.launch {
                        showProgressBar()
                        appRepository.setToken(it.result?.token)
                        val token = appRepository.currentToken.value
                        withContext(Dispatchers.IO) {
                            if (token != null) {
                                try {
                                    val request =
                                        appRepository.getCurrentUserAsync(token).await()

                                    if (request.isSuccessful) {

                                        val data = request.body()
                                        if (appRepository.isUserExist(data!!.uid)) {

                                            withContext(Dispatchers.Main) {

                                                appRepository.getUser().addSource(
                                                    appRepository.getUser(data.uid),
                                                    appRepository.getUser()::setValue
                                                )
                                            }
                                        } else {

                                            appRepository.insertUser(data)
                                            withContext(Dispatchers.Main) {
                                                appRepository.getUser().addSource(
                                                    appRepository.getUser(data.uid),
                                                    appRepository.getUser()::setValue
                                                )
                                            }
                                        }
                                        val request2 =
                                            appRepository.getClassroomsAsync(token).await()
                                        if (request2.isSuccessful) {
                                            val classroomsData = request2.body()
                                            Timber.wtf("data -------->  $classroomsData")
                                            classroomsData?.forEach { classroomEntry ->
                                                setListeners(classroomEntry.key)

                                            }
                                        } else Timber.wtf(" else 1 ${request2.code()}${request2.errorBody()} ${request2.message()}")
                                    } else Timber.wtf("else 2 ${request.code()}${request.errorBody()}")
                                } catch (e: Exception) {
                                    Timber.e(" catch clause -> ${e.printStackTrace()}${e.message}")
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
                    snapshot.toObject(ClassroomFirestore::class.java)
                Timber.wtf("classroom -> $classroomFirestore")
                classroomFirestore?.let {
                    val updatedClassroom = Classroom(
                        classroomFirestore.uid,
                        classroomFirestore.name,
                        classroomFirestore.owner,
                        classroomFirestore.ownerUid,
                        classroomFirestore.userList,
                        classroomFirestore.dateCreated.toString(),
                        classroomFirestore.description,
                        classroomFirestore.numberOfTasks
                    )
                    Timber.wtf("formatted classroom is -> $updatedClassroom")
                    adapterList[updatedClassroom.uid] = updatedClassroom
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