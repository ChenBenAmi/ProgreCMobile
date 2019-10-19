package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.beans.Classroom
import com.example.progresee.data.AppRepository
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class CreateClassroomViewModel(
    private val appRepository: AppRepository,
    private val classroomId: String?
) : BaseViewModel() {


    private var token: LiveData<String?>


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val classroom = MediatorLiveData<Classroom>()
    fun getClassroom() = classroom

    private val _navigateBackToClassroomFragment = MutableLiveData<Long?>()
    val navigateBackToClassroomFragment: LiveData<Long?>
        get() = _navigateBackToClassroomFragment

    private val _stringLength = MutableLiveData<Int?>()
    val stringLength: LiveData<Int?>
        get() = _stringLength

    private val _showProgressBar=MutableLiveData<Boolean?>()
    val showProgressBar
    get() = _showProgressBar

    init {
        if (classroomId !=null) {
            classroom.addSource(appRepository.getClassroom(classroomId), classroom::setValue)
        }
        token = appRepository.currentToken
    }

    fun onSavePressed(name: String) {
        when {
            name.length > 60 -> _stringLength.value = 1
            name.isEmpty() -> _stringLength.value = 2
            else -> uiScope.launch {
                showProgressBar()
                withContext(Dispatchers.IO) {
                    if (classroom.value == null) {
                        try {
                            val request = appRepository.createClassroomAsync(token.value, name).await()
                            if (request.isSuccessful) {
                                val data = request.body()
                                appRepository.insertClassroom(data)
                                withContext(Dispatchers.Main) {
                                    hideProgressBar()
                                    _navigateBackToClassroomFragment.value = 0
                                }
                            }
                        } catch (e: Exception) {
                            Timber.wtf("${e.message}${e.printStackTrace()}")
                        }
                    } else {
                        val classroom = getClassroom().value
                        if (classroom!=null) {
                            try {
                                classroom.name=name
                                val request = appRepository.updateClassroomAsync(token.value, classroom).await()
                                if (request.isSuccessful) {
                                    val data = request.body()
                                    appRepository.updateClassroom(data)
                                    withContext(Dispatchers.Main) {
                                        hideProgressBar()
                                        _navigateBackToClassroomFragment.value = 0
                                    }
                                } else {
                                    Timber.wtf("${request.code()}${request.raw()}")
                                }
                            } catch (e: Exception) {
                                Timber.wtf("oh no something went wrong!")
                            }
                        }
                    }
                }
            }
        }
    }



    override fun showProgressBar(){
        _showProgressBar.value=true
    }

     override fun hideProgressBar() {
        _showProgressBar.value=null
    }

     override fun snackBarShown() {
        _stringLength.value = null
    }

    override fun onDoneNavigating() {
        _navigateBackToClassroomFragment.value = null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }




}