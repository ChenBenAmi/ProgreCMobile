package com.example.progresee.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.data.AppRepository

class HomeViewModel constructor(private val appRepository: AppRepository): ViewModel() {


    private val _navigateToFirebaseLoginFragment = MutableLiveData<Boolean?>()

    val navigateToFirebaseLoginFragment: LiveData<Boolean?>
        get() = _navigateToFirebaseLoginFragment




    fun doneNavigating() {
        _navigateToFirebaseLoginFragment.value = null
    }

    fun navigate() {
        _navigateToFirebaseLoginFragment.value = true
    }
}