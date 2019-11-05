package com.example.progresee.viewmodels

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.R
import com.example.progresee.beans.Exercise
import com.example.progresee.beans.Task
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber

class TaskDetailsViewModel constructor(
    private val appRepository: AppRepository,
    private val classroomId: String,
    private val taskId: String
) :
    BaseViewModel() {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val task = MediatorLiveData<Task>()
    fun getTask() = task

    private val exercises = MediatorLiveData<List<Exercise>>()
    fun getExercises() = exercises

    private val _changeExerciseStatus = MutableLiveData<String?>()
    val changeExerciseStatus
        get() = _changeExerciseStatus

    private val _navigateTooTaskFragment = MutableLiveData<Boolean?>()
    val navigateToTaskFragment
        get() = _navigateTooTaskFragment

    private val _navigateToUsersFinished = MutableLiveData<String?>()
    val navigateToUsersFinished
        get() = _navigateToUsersFinished

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar
        get() = _showSnackBar

    private val _editExercise = MutableLiveData<Exercise?>()
    val editExercise
        get() = _editExercise

    private val _removeExercise = MutableLiveData<String?>()
    val removeExercise
        get() = _removeExercise

    private val _createExerciseAlert = MutableLiveData<Boolean?>()
    val createExerciseAlert
        get() = _createExerciseAlert


    init {
        task.addSource(appRepository.getTask(taskId), task::setValue)
        fetchExercisesFromFirebase()
    }

    fun onTaskClicked(exerciseId: String) {
        _changeExerciseStatus.value = exerciseId
    }

    private suspend fun insertExercise(exercise: Exercise) {
        withContext(Dispatchers.IO) {
            appRepository.insertExercise(exercise)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun fetchExercisesFromFirebase() {
        uiScope.launch {
            Timber.wtf("hey")
            showProgressBar()
            withContext(Dispatchers.IO) {
                Timber.wtf("hey1")
                if (appRepository.currentToken.value != null) {
                    try {
                        Timber.wtf("hey2")
                        val response = appRepository.getAllExercisesAsync(
                            appRepository.currentToken.value!!,
                            classroomId, taskId
                        ).await()
                        Timber.wtf("hey3")
                        if (response.isSuccessful) {
                            Timber.wtf("hey4")
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.insertExercise(it.value)
                            }
                            Timber.wtf("hey5")
                            withContext(Dispatchers.Main) {
                                exercises.addSource(
                                    appRepository.getExercises(taskId),
                                    exercises::setValue
                                )
                            }
                            Timber.wtf("hey6")
                        }
                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }
            hideProgressBar()
        }
    }

    fun deleteTask() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.deleteTaskAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            taskId
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.deleteTaskById(taskId)
                            }
                        }
                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }
            hideProgressBar()
            navigate()
        }

    }

    fun addExercise(description: String) {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.createExerciseAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            taskId, description
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.insertExercise(it.value)
                            }
                            withContext(Dispatchers.Main) {
                                hideProgressBar()
                                showSnackBar()
                            }
                        }

                    } catch (e: Exception) {
                        Timber.wtf("Something went wrong${e.printStackTrace()}${e.message}")
                    }
                }
            }


        }
    }

    fun deleteExercise(uid: String) {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.deleteExerciseAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            taskId,
                            uid
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.deleteExerciseById(uid)
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

    fun updateExercise(exercise: Exercise, newDescription: String) {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        Timber.wtf("hey")
                        exercise.exerciseTitle = newDescription
                        val response = appRepository.updateExerciseAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            taskId,
                            exercise
                        ).await()
                        Timber.wtf("hey1")
                        if (response.isSuccessful) {
                            Timber.wtf("hey2")
                            val data = response.body()
                            Timber.wtf(data.toString())
                            data?.forEach {
                                appRepository.updateExercise(it.value)
                                Timber.wtf("hey3")
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

    fun onExerciseClicked(exercise: Exercise, context: Context, view: View) {
        val popup = PopupMenu(context, view)
        popup.inflate(R.menu.exercise_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.edit_menu_item_exercise -> {
                    showEditExerciseDialog(exercise)
                }
                R.id.delete_menu_item_exercise -> {
                    showRemoveExerciseDialog(exercise.uid)
                }
                R.id.users_finished_list -> {
                    navigatingToUsersFinished(exercise.uid)
                }
            }
            true
        }
        popup.show()
    }

    fun showEditExerciseDialog(exercise: Exercise) {
        _editExercise.value = exercise
    }

    fun hideEditExerciseDialog() {
        _editExercise.value = null
    }

    fun showRemoveExerciseDialog(uid: String) {
        _removeExercise.value = uid
    }

    fun hideRemoveExerciseDialog() {
        _removeExercise.value = null
    }

    override fun showProgressBar() {
        _showProgressBar.value = true
    }

    override fun hideProgressBar() {
        _showProgressBar.value = null
    }

    override fun onDoneNavigating() {
        _navigateTooTaskFragment.value = null
    }

    override fun navigate() {
        _navigateTooTaskFragment.value = true
    }

    fun onDoneNavigatingToUsersFinished() {
        _navigateToUsersFinished.value = null
    }

    fun navigatingToUsersFinished(uid: String) {
        _navigateToUsersFinished.value = uid
    }

    fun showSnackBar() {
        _showSnackBar.value = true
    }

    override fun snackBarShown() {
        _showSnackBar.value = null
    }

    fun showCreateExerciseAlert() {
        _createExerciseAlert.value = true
    }


    fun hideCreateExerciseAlert() {
        _createExerciseAlert.value = null
    }


}