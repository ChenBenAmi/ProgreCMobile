package com.example.progresee.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class SplashViewModel constructor(application: Application) : AndroidViewModel(application) {


    private val _navigateToLoginFragment = MutableLiveData<Boolean?>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val navigateToLoginFragment: LiveData<Boolean?>
        get() = _navigateToLoginFragment


    fun doneNavigating() {
        _navigateToLoginFragment.value = null
    }


    fun navigate() {
        _navigateToLoginFragment.value = true
    }

    val liveData: LiveData<SplashState>
        get() = mutableLiveData
    private val mutableLiveData = MutableLiveData<SplashState>()

    init {
        uiScope.launch {
            delay(3000)
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

