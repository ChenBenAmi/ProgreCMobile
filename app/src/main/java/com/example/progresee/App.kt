package com.example.progresee

import android.app.Application
import androidx.room.Room
import com.example.progresee.data.database.AppDatabase
import com.example.progresee.data.AppRepository
import com.example.progresee.data.network.ApiService
import com.example.progresee.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())


        val appModule = module {
            single {
                Room.databaseBuilder(get(), AppDatabase::class.java, "ProgreSeeDB")
                    .fallbackToDestructiveMigration().build()
            }
            single { ApiService() }
            single { get<AppDatabase>().userDao() }
            single { get<AppDatabase>().classroomDao() }
            single { get<AppDatabase>().taskDao() }
            single { get<AppDatabase>().exerciseDao() }
            single { get<AppDatabase>().finishedUsersDao() }
            single { AppRepository(get(),get()) }
            viewModel { (appRepository: AppRepository, classroomId: Long) ->
                TaskViewModel(
                    appRepository,
                    classroomId
                )
            }
            viewModel { (appRepository: AppRepository) -> CreateClassroomViewModel(appRepository) }
            viewModel { (appRepository: AppRepository) -> ClassroomViewModel(appRepository) }
            viewModel { SplashViewModel() }
            viewModel { (appRepository: AppRepository)->LoginViewModel(appRepository) }


        }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)

        }
    }
}