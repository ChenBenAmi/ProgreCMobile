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
import java.util.*

class CreateClassroomViewModel(
    private val appRepository: AppRepository,
    private val classroomId: Long
) : ViewModel() {


    private lateinit var token: LiveData<String?>


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

    init {
        if (classroomId > 0) {
            classroom.addSource(appRepository.getClassroom(classroomId), classroom::setValue)
        }
        token = appRepository.currentToken
    }

    fun onSavePressed(name: String) {
        when {
            name.length > 60 -> _stringLength.value = 1
            name.isEmpty() -> _stringLength.value = 2
            else -> uiScope.launch {
                withContext(Dispatchers.IO) {
                    Timber.wtf("value is ${token.value} & ${token} ")
                    if (classroom.value == null) {
                        try {
                            Timber.wtf("w8ing")
                            val request = appRepository.createClassroom(token.value, "hey").await()
                            Timber.wtf(request.message())
                            if (request.isSuccessful) {
                                Timber.wtf("hey123123123123")
                                val data = request.body()
                                val tempClassroom=Classroom(data!!.id,data.name,data.owner,data.dateCreated,0)
                                appRepository.insertClassroom(tempClassroom)
                                withContext(Dispatchers.Main) {
                                    _navigateBackToClassroomFragment.value = 0
                                }
                            }
                            Timber.wtf("WHY?!")
                        } catch (e: Exception) {
                            Timber.wtf(e.message+e.printStackTrace())
                        }
                    } else {

                        val classroom = appRepository.getClassroom(classroomId)
                        Timber.wtf("classroom value is ${classroom} & ${classroom.value}")
                        val tempClassroom = classroom.value
                        Timber.wtf("tempCLASSROOM $tempClassroom")
                        if (tempClassroom != null) {
                            try {
                                Timber.wtf("try2")

                                val request =
                                    appRepository.updateClassroom(token.value, tempClassroom)
                                        .await()
                                if (request.isSuccessful) {
                                    Timber.wtf("sucxxxxx")

                                    val data = request.body()
                                    val tempClassroom=Classroom(data!!.id,data.name,data.owner,data.dateCreated,0)
                                    appRepository.updateClassroom(tempClassroom)
                                    withContext(Dispatchers.Main) {
                                        //Timber.wtf("classroom object is ffs and ${classroom.value}")
                                        _navigateBackToClassroomFragment.value = 0
                                    }
                                }
                            } catch (e: Exception) {
                                Timber.wtf("oh no kenny is dead")
                            }
                        }
                    }
                }
            }
        }
    }

    fun snackBarShown() {
        _stringLength.value = null
    }

    fun onDoneNavigating() {
        _navigateBackToClassroomFragment.value = null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }




}