package com.app.progrec.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.progrec.beans.Classroom
import com.app.progrec.data.AppRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.*
import timber.log.Timber

class CreateClassroomViewModel(
    private val appRepository: AppRepository, classroomId: String
) : BaseViewModel() {


    private var token: LiveData<String?>
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val classroom = MutableLiveData<Classroom>()
    fun getClassroom() = classroom

    private val _navigateBackToClassroomFragment = MutableLiveData<Long?>()
    val navigateBackToClassroomFragment: LiveData<Long?>
        get() = _navigateBackToClassroomFragment

    private val _stringLength = MutableLiveData<Int?>()
    val stringLength: LiveData<Int?>
        get() = _stringLength

    private val _descriptionStringLength = MutableLiveData<Int?>()
    val descriptionStringLength: LiveData<Int?>
        get() = _descriptionStringLength

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBarHttpError = MutableLiveData<Int?>()
    val showSnackBarHttpError
        get() = _showSnackBarHttpError

    private val listenersList= mutableListOf<ListenerRegistration>()

    init {
        if (classroomId != "none") {
            setListeners(classroomId)
        }
        token = appRepository.currentToken
    }

    private fun setListeners(uid: String) {
        val db = appRepository.getFirestoreDB()
        val docRef = db.collection("classrooms")
            .document(uid)

       val listener= docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Timber.wtf("Listen failed $e")
            }
            if (snapshot != null && snapshot.exists()) {
                val classroomFirestore =
                    snapshot.toObject(Classroom::class.java)
                Timber.wtf("classroom -> $classroomFirestore")
                classroomFirestore?.let {
                    classroom.value = it

                }
            } else {
                Timber.wtf("Current data: null")
            }
        }
        listenersList.add(listener)
    }

    fun onSavePressed(name: String, description: String) {
        when {
            name.length > 32 -> _stringLength.value = 1
            name.isEmpty() -> _stringLength.value = 2
            description.length > 100 -> _descriptionStringLength.value = 1
            description.isEmpty() -> _descriptionStringLength.value = 2
            else -> uiScope.launch {
                showProgressBar()
                withContext(Dispatchers.IO) {
                    if (classroom.value == null) {
                        if (token.value != null) {
                            try {
                                val request =
                                    appRepository.createClassroomAsync(
                                        token.value!!,
                                        name,
                                        description
                                    ).await()
                                if (request.isSuccessful) {
                                    val data = request.body()
                                    Timber.wtf(data.toString())
                                    data?.let {
                                        withContext(Dispatchers.Main) {
                                            _navigateBackToClassroomFragment.value = 0
                                        }
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        showHttpErrorSnackBar400()
                                    }
                                }
                            } catch (e: Exception) {
                                Timber.wtf("${e.message}${e.printStackTrace()}")
                                withContext(Dispatchers.Main) {
                                    showHttpErrorSnackBarNetwork()
                                    hideProgressBar()
                                }
                            } finally {
                                withContext(Dispatchers.Main) {
                                    hideProgressBar()
                                }
                            }
                        }
                    } else {
                        val classroom = getClassroom().value
                        Timber.wtf(classroom.toString())
                        if (classroom != null) {
                            try {
                                classroom.name = name
                                classroom.description = description
                                Timber.wtf("name is $name desc $description")
                                val request =
                                    appRepository.updateClassroomAsync(
                                        token.value!!,
                                        classroom.uid,
                                        classroom.name,
                                        classroom.description
                                    )
                                        .await()
                                if (request.isSuccessful) {
                                    val data = request.body()
                                    data?.let {
                                        _navigateBackToClassroomFragment.value = 0
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        showHttpErrorSnackBar400()
                                    }
                                    Timber.wtf("${request.code()}${request.raw()}")
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    showHttpErrorSnackBarNetwork()
                                }
                                Timber.wtf("oh no something went wrong!${e.printStackTrace()}${e.message}")
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
    }


    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    override fun snackBarShown() {
        _stringLength.value = null
        _descriptionStringLength.value = null
    }

    override fun onDoneNavigating() {
        _navigateBackToClassroomFragment.value = null
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
        listenersList.forEach {
            it.remove()
        }
    }


}