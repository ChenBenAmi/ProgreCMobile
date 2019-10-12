package com.example.progresee.viewmodels

import androidx.lifecycle.ViewModel
import com.example.progresee.data.AppRepository

class LoginViewModel (private val appRepository: AppRepository):ViewModel() {

    fun getCurrentUser(token: String?) {
        appRepository.getCurrentUser(token)
    }

}