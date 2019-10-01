package com.example.progresee

import android.app.Application
import timber.log.Timber

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant()
    }
}