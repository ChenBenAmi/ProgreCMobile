package com.example.progresee.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.*
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber

class TaskViewModel(private val appRepository: AppRepository, private val classroomId: String) :
    BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private var _isAdmin = appRepository.isAdmin
    val isAdmin
        get() = _isAdmin

    private val adapterList = hashMapOf<String, Task>()

    private val _tasks=MutableLiveData<List<Task>>()
    val tasks
        get() = _tasks

    private val classroom = MutableLiveData<Classroom>()
    fun getClassroom() = classroom

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar
        get() = _showSnackBar


    private val _navigateTooTaskDetailsFragment = MutableLiveData<String>()
    val navigateToTaskDetailsFragment
        get() = _navigateTooTaskDetailsFragment

    private val _navigateToCreateTask = MutableLiveData<Boolean?>()
    val navigateToCreateTask
        get() = _navigateToCreateTask

    private val _navigateBackToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateBackToClassroomFragment
        get() = _navigateBackToClassroomFragment


    init {
        uiScope.launch {
            fetchTasksFromFirebase()
            setClassroomListener(classroomId)
        }
    }

    private fun setClassroomListener(uid: String) {
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
                    classroom.value=it

                }
            } else {
                Timber.wtf("Current data: null")
            }
        }
    }

    private fun setTaskListeners(uid: String) {
        val db = appRepository.getFirestoreDB()
        val docRef = db.collection("tasks")
            .document(uid)

        docRef.addSnapshotListener { snapshot, e ->

            if (e != null) {
                Timber.wtf("Listen failed $e")
            }
            if (snapshot != null && snapshot.exists()) {
                Timber.wtf("Current data: ${snapshot.data}")

                val taskFirestore =
                    snapshot.toObject(Task::class.java)
                Timber.wtf("task -> $taskFirestore")
                taskFirestore?.let {
                    Timber.wtf("formatted classroom is -> $it")
                    adapterList[it.uid] = it
                    tasks.value = adapterList.values.toList()

                }
            } else {
                Timber.wtf("Current data: null")
            }
        }
    }

    private fun fetchTasksFromFirebase() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.getAllTasksAsync(
                            appRepository.currentToken.value!!,
                            classroomId
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                setTaskListeners(it.key)
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

    fun deleteClassRoom() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.deleteClassroomAsync(
                            appRepository.currentToken.value!!,
                            classroom.value!!.uid
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
//                                appRepository.deleteClassroomById(it.value)
                            }
                        }
                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }
            hideProgressBar()
            onClassroomDeleted()
        }

    }


    fun addToClassRoom(text: String) {
        Timber.wtf(text)
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.addToClassroomAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            text
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data != null) {
                                Timber.wtf(data.toString())
                                data.forEach {
//                                    appRepository.insertClassroom(it.value)
                                }
                                withContext(Dispatchers.Main) {
                                    hideProgressBar()
                                    showSnackBar()
                                }
                            }
                        } else {
                            Timber.wtf("${response.code()}${response.errorBody()}")
                        }
                    } catch (e: Exception) {
                        Timber.wtf("${e.message}${e.printStackTrace()}")
                    }
                }
            }
        }
    }





    fun onTaskClicked(id: String) {
        _navigateTooTaskDetailsFragment.value = id
    }

    fun doneNavigateToTaskDetailsFragment() {
        _navigateTooTaskDetailsFragment.value = null
    }

    private fun onClassroomDeleted() {
        _navigateBackToClassroomFragment.value = true
    }

    fun doneNavigateToClassroomFragment() {
        _navigateBackToClassroomFragment.value = null
    }

    fun navigateToCreateTaskFragment() {
        _navigateToCreateTask.value = true
    }

    fun doneNavigationToCreateTaskFragment() {
        _navigateToCreateTask.value = null
    }

    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    fun showSnackBar() {
        _showSnackBar.value = true
    }

    override fun snackBarShown() {
        _showSnackBar.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        uiScope.cancel()
    }


}