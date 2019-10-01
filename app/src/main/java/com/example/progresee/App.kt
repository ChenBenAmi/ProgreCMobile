package com.example.progresee

import android.app.Application
import com.example.progresee.di.AppComponent
import com.example.progresee.di.AppModule
import com.example.progresee.di.DaggerAppComponent
import timber.log.Timber

class App:Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant()

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}