package com.example.progresee.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.data.AppRepository

class LoginViewModel constructor(private val appRepository: AppRepository): ViewModel() {


    private val _navigateToClassRoomFragment = MutableLiveData<Boolean?>()

    val navigateToClassRoomFragment: LiveData<Boolean?>
        get() = _navigateToClassRoomFragment


    fun loginWithGoogle(){
        appRepository.loginWithGoogle()
    }

    fun doneNavigating() {
        _navigateToClassRoomFragment.value = null
    }

    fun navigate() {
        _navigateToClassRoomFragment.value = true
    }
}