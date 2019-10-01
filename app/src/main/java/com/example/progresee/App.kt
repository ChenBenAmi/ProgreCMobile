package com.example.progresee

import android.app.Application
import com.example.progresee.viewmodels.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant()


        val appModule = module {
            viewModel { SplashViewModel(get()) }
        }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}