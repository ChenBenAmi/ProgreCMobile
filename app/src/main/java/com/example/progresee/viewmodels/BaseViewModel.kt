package com.example.progresee.viewmodels

import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    open fun showProgressBar() {}

    open fun hideProgressBar() {}

    open fun snackBarShown() {}

    open fun onDoneNavigating() {}

    open fun navigate() {}

    open fun showHttpErrorSnackBar400() {}

    open fun showHttpErrorSnackBarNetwork() {}

    open fun showHttpErrorSnackBarServer() {}

    open fun hideHttpErrorSnackBar() {}






}