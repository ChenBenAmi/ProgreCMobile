package com.example.progresee.viewmodels

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.progresee.R
import com.example.progresee.beans.*
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

    private var _isAdmin = appRepository.isAdmin
    val isAdmin
        get() = _isAdmin

    private var _isEmpty = MutableLiveData<Boolean?>()
    val isEmpty
        get() = _isEmpty

    private val checkedList = mutableListOf<String>()
    fun getCheckedList() = checkedList

    private val task = MutableLiveData<Task>()
    fun getTask() = task

    private val classroom = MutableLiveData<Classroom>()
    fun getClassroom() = classroom

    private val adapterList = hashMapOf<String, Exercise>()

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises
        get() = _exercises

    private val _changeExerciseStatus = MutableLiveData<String?>()
    val changeExerciseStatus
        get() = _changeExerciseStatus

    private val _navigateTooTaskFragment = MutableLiveData<Boolean?>()
    val navigateToTaskFragment
        get() = _navigateTooTaskFragment

    private val _navigateToUsersFinished = MutableLiveData<String?>()
    val navigateToUsersFinished
        get() = _navigateToUsersFinished

    private val _navigateToClassroomFragment = MutableLiveData<Boolean?>()
    val navigateToClassroomFragment
        get() = _navigateToClassroomFragment

    private val _showProgressBar = MutableLiveData<Boolean?>()
    val showProgressBar
        get() = _showProgressBar

    private val _showSnackBar = MutableLiveData<Boolean?>()
    val showSnackBar
        get() = _showSnackBar

    private val _showSnackBarClassroom = MutableLiveData<Boolean?>()
    val showSnackBarClassroom
        get() = _showSnackBarClassroom

    private val _showSnackBarTask = MutableLiveData<Boolean?>()
    val showSnackBarTask
        get() = _showSnackBarTask

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
        setClassroomListener(classroomId)
        setTaskListeners(taskId)
        fetchExercisesFromFirebase()
    }

    fun onTaskClicked(exerciseId: String) {
        _changeExerciseStatus.value = exerciseId
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
                    if (!it.archived) {
                        Timber.wtf("formatted classroom is -> $it")
                        classroom.value = it
                    } else {
                        if (appRepository.isAdmin.value == false) {
                            showSnackBarClassroom()
                            navigatingToClassroomFragment()
                        }
                    }
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
                    if (!it.archived) {
                        Timber.wtf("formatted task is -> $it")
                        task.value = it
                    } else {
                        if (appRepository.isAdmin.value == false) {
                            showSnackBarTask()
                            navigate()
                        }
                    }
                }
            } else {
                Timber.wtf("Current data: null")
            }
        }
    }

    private fun setExerciseListeners(uid: String) {
        val db = appRepository.getFirestoreDB()
        val docRef = db.collection("exercises")
            .document(uid)
        docRef.addSnapshotListener { snapshot, e ->

            if (e != null) {
                Timber.wtf("Listen failed $e")
            }
            if (snapshot != null && snapshot.exists()) {
                Timber.wtf("Current data: ${snapshot.data}")

                val exerciseFirestore =
                    snapshot.toObject(Exercise::class.java)
                Timber.wtf("exercise -> $exerciseFirestore")
                exerciseFirestore?.let {
                    if (!it.archived) {
                        adapterList[exerciseFirestore.uid] = exerciseFirestore
                        exercises.value = adapterList.values.toList()
                    }
                }
            } else {
                Timber.wtf("Current data: null")
            }
        }
    }


    fun fetchExercisesFromFirebase() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        val response = appRepository.getAllExercisesAsync(
                            appRepository.currentToken.value!!,
                            classroomId, taskId
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.let {

                                if (it.isNotEmpty()) {
                                    withContext(Dispatchers.Main) {
                                        _isEmpty.value = false
                                    }
                                    data.forEach { exercise ->
                                        setExerciseListeners(exercise.key)
                                    }
                                } else {
                                    withContext(Dispatchers.Main) {
                                        _isEmpty.value = true
                                        Timber.wtf("no exercises available ")

                                    }
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                _isEmpty.value = true
                                Timber.wtf("no exercises available ${response.code()}")
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
                            data?.let {
                                navigate()
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
                            data?.let {
                                data.forEach {
                                    setExerciseListeners(it.key)
                                }
                                withContext(Dispatchers.Main) {
                                    _isEmpty.value = null
                                }
                            }
                            withContext(Dispatchers.Main) {
                                showSnackBar()
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
                            data?.forEach { _ ->
                                fetchExercisesFromFirebase()
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

    fun updateExercise(exercise: Exercise, newDescription: String) {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        exercise.exerciseTitle = newDescription
                        val response = appRepository.updateExerciseAsync(
                            appRepository.currentToken.value!!,
                            classroomId,
                            taskId,
                            exercise
                        ).await()
                        if (response.isSuccessful) {
                            val data = response.body()
                            data?.forEach {

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

    fun updateExercisesStatus() {
        uiScope.launch {
            showProgressBar()
            withContext(Dispatchers.IO) {
                if (appRepository.currentToken.value != null) {
                    try {
                        checkedList.forEach { uid ->
                            val response = appRepository.updateStatusAsync(
                                appRepository.currentToken.value!!,
                                classroomId,
                                taskId,
                                uid
                            ).await()
                            if (response.isSuccessful) {
                                val data = response.body()
                                Timber.wtf(data.toString())
                               data?.let {
                                   checkedList.clear()
                                   Timber.wtf(checkedList.toString())
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

    fun onExerciseChecked(it: Exercise) {
        if (checkedList.contains(it.uid)) {
            checkedList.remove(it.uid)
        } else {
            checkedList.add(it.uid)
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

    private fun showEditExerciseDialog(exercise: Exercise) {
        _editExercise.value = exercise
    }

    fun hideEditExerciseDialog() {
        _editExercise.value = null
    }

    private fun showRemoveExerciseDialog(uid: String) {
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

    fun showSnackBarClassroom() {
        _showSnackBarClassroom.value = true
    }

    fun hideSnackBarClassroom() {
        _showSnackBarClassroom.value = null
    }

    fun showSnackBarTask() {
        _showSnackBarTask.value = true
    }

    fun hideSnackBarTask() {
        _showSnackBarTask.value = null
    }

    fun onDoneNavigatingToUsersFinished() {
        _navigateToUsersFinished.value = null
    }

    private fun navigatingToUsersFinished(uid: String) {
        _navigateToUsersFinished.value = uid
    }

    fun onDoneNavigatingToClassroomFragment() {
        _navigateToClassroomFragment.value = null
    }

    private fun navigatingToClassroomFragment() {
        _navigateToClassroomFragment.value = true
    }

    private fun showSnackBar() {
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}