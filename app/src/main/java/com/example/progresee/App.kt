package com.example.progresee

import android.app.Application
import androidx.room.Room
import com.example.progresee.data.database.AppDatabase
import com.example.progresee.data.AppRepository
import com.example.progresee.viewmodels.ClassroomViewModel
import com.example.progresee.viewmodels.LoginViewModel
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
            single { Room.databaseBuilder(get(),AppDatabase::class.java,"ProgreSeeDB").fallbackToDestructiveMigration().build() }
            single { get<AppDatabase>().userDao() }
            single { get<AppDatabase>().classroomDao() }
            single { get<AppDatabase>().taskDao() }
            single { get<AppDatabase>().exerciseDao() }
            single { get<AppDatabase>().finishedUsersDao() }
            single { AppRepository(get()) }
            viewModel { (appRepository: AppRepository)->ClassroomViewModel(get(),appRepository) }
            viewModel { SplashViewModel(get()) }
            viewModel { LoginViewModel(get()) }



        }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)

        }
    }
}