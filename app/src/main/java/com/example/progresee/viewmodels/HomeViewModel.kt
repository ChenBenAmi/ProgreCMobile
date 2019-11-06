package com.example.progresee.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.progresee.data.AppRepository

class HomeViewModel constructor(private val appRepository: AppRepository): BaseViewModel() {


    private val _navigateToFirebaseLoginFragment = MutableLiveData<Boolean?>()

    val navigateToFirebaseLoginFragment: LiveData<Boolean?>
        get() = _navigateToFirebaseLoginFragment

    override fun onDoneNavigating() {
        _navigateToFirebaseLoginFragment.value = null
    }

    override fun navigate() {
        _navigateToFirebaseLoginFragment.value = true
    }
}