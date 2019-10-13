package com.example.progresee.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class SplashViewModel  : BaseViewModel() {


    private val _navigateToLoginFragment = MutableLiveData<Boolean?>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val navigateToLoginFragment: LiveData<Boolean?>
        get() = _navigateToLoginFragment


    fun doneNavigating() {
        _navigateToLoginFragment.value = null
    }


    override fun navigate() {
        _navigateToLoginFragment.value = true
    }

    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()

    init {
        uiScope.launch {
            delay(100)
            mutableLiveData.postValue(SplashState.MainActivity)
            navigate()
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}



sealed class SplashState {
    object MainActivity : SplashState()
}

