package com.example.progresee.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule (private val app:Application) {
    @Provides
    @Singleton
    fun providesAppContext(): Context {
        return app.applicationContext
    }
}