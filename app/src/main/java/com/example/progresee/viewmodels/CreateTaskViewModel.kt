package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.beans.Task
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception
import java.util.*

class CreateTaskViewModel(
    private val appRepository: AppRepository,
    private val classroomId: String,
    private val taskId: String?

) : BaseViewModel() {
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val task = MutableLiveData<Task>()
    fun getTask() = task


    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _navigateBackToTaskFragment = MutableLiveData<Boolean?>()
    val navigateBackToTaskFragment
        get() = _navigateBackToTaskFragment

    private val _pickDate = MutableLiveData<Boolean?>()
    val pickDate
        get() = _pickDate

    private val _stringLength = MutableLiveData<Int?>()
    val stringLength: LiveData<Int?>
        get() = _stringLength

    private val _descriptionStringLength = MutableLiveData<Int?>()
    val descriptionStringLength: LiveData<Int?>
        get() = _descriptionStringLength


    init {
        if (taskId != null) {
            setTaskListeners(taskId)
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
                Timber.wtf("classroom -> $taskFirestore")
                taskFirestore?.let {
                    Timber.wtf("formatted classroom is -> $it")
                    task.value=it
                }
            } else {
                Timber.wtf("Current data: null")
            }
        }
    }

    fun onPressedDatePick() {
        _pickDate.value = true
    }

    fun onPickDateFinished() {
        _pickDate.value = null
    }

    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    override fun navigate() {
        _navigateBackToTaskFragment.value = true
    }

    override fun onDoneNavigating() {
        _navigateBackToTaskFragment.value = null
    }

    override fun snackBarShown() {
        _stringLength.value = null
        _descriptionStringLength.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onSavePressed(title: String, description: String, link: String, date: String) {
        Timber.wtf("hey")
        Timber.wtf(date)
        var sentDate: String = ""
        if (date == "00/00/0000") {
            sentDate = "$day/${month.plus(1)}/$year"
        } else {
            sentDate = date
        }
        when {
            title.length > 60 -> _stringLength.value = 1
            title.isEmpty() -> _stringLength.value = 2
            description.length > 100 -> _descriptionStringLength.value = 1
            description.isEmpty() -> _descriptionStringLength.value = 2
            else -> uiScope.launch {
                Timber.wtf("hey1")
                showProgressBar()
                withContext(Dispatchers.IO) {
                    if (taskId == null) {
                        if (appRepository.currentToken.value != null) {
                            try {
                                val request =
                                    appRepository.createTaskAsync(
                                        appRepository.currentToken.value!!,
                                        classroomId,
                                        title,
                                        description,
                                        link,
                                        sentDate
                                    ).await()
                                Timber.wtf("hey2")

                                if (request.isSuccessful) {
                                    Timber.wtf("heySux3")

                                    val data = request.body()
                                    Timber.wtf(data.toString())
                                    data?.forEach {
//                                        appRepository.insertTask(it.value)
                                    }


                                    withContext(Dispatchers.Main) {
                                        hideProgressBar()
                                        _navigateBackToTaskFragment.value = true
                                    }
                                }
                            } catch (e: Exception) {
                                Timber.wtf("${e.message}${e.printStackTrace()}")
                            }
                        }
                    } else {
                        val task = getTask().value

                        Timber.wtf(task.toString())
                        if (task != null) {
                            try {
                                task.title = title
                                task.description = description
                                task.endDate = sentDate
                                task.referenceLink = link
                                val request =
                                    appRepository.updateTaskAsync(
                                        appRepository.currentToken.value!!,
                                        classroomId,
                                        task
                                    )
                                        .await()
                                if (request.isSuccessful) {
                                    val data = request.body()
                                    data?.forEach {
//                                        appRepository.updateTask(task)
                                    }

                                    withContext(Dispatchers.Main) {
                                        hideProgressBar()
                                        navigateBackToTaskFragment.value = true
                                    }
                                } else {
                                    Timber.wtf("${request.code()}${request.raw()}")
                                }
                            } catch (e: Exception) {
                                Timber.wtf("oh no something went wrong!${e.printStackTrace()}${e.message}")
                            }
                        }
                    }
                }
            }
        }
    }
}