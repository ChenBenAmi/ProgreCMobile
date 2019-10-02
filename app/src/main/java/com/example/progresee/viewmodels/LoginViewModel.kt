package com.example.progresee.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel constructor(application: Application): AndroidViewModel(application) {


    private val _navigateToClassRoomFragment = MutableLiveData<Boolean?>()

    val navigateToClassRoomFragment: LiveData<Boolean?>
        get() = _navigateToClassRoomFragment



    fun doneNavigating() {
        _navigateToClassRoomFragment.value = null
    }

    fun navigate() {
        _navigateToClassRoomFragment.value = true
    }
}