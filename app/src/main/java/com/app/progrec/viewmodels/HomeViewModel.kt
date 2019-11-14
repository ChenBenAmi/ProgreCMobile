package com.app.progrec.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.progrec.data.AppRepository

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