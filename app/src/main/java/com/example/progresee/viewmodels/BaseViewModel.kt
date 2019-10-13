package com.example.progresee.viewmodels

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    open fun showProgressBar() {}

    open fun hideProgressBar() {}

    open fun snackBarShown() {}

    open fun onDoneNavigating() {}

    open fun navigate() {}




}