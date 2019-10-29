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
            single { AppRepository(get(), get()) }

            viewModel { SplashViewModel() }
            viewModel { (appRepository: AppRepository) -> HomeViewModel(appRepository) }
            viewModel { (appRepository: AppRepository) -> LoginViewModel(appRepository) }

            viewModel { (appRepository: AppRepository) ->
                ClassroomViewModel(
                    appRepository
                )
            }
            viewModel { (appRepository: AppRepository, classroomId: String) ->
                CreateClassroomViewModel(
                    appRepository,
                    classroomId
                )
            }

            viewModel { (appRepository: AppRepository, classroomId: String) ->
                TaskViewModel(
                    appRepository,
                    classroomId
                )
            }
            viewModel { (appRepository: AppRepository, classroomId: String,taskId:String) ->
                TaskDetailsViewModel(
                    appRepository,
                    classroomId,taskId
                )
            }
            viewModel { (appRepository: AppRepository, classroomId: String, owner: Boolean) ->
                UserViewModel(
                    appRepository,
                    classroomId, owner
                )
            }

            viewModel { (classroomId: String, taskId: String) ->
                CreateTaskViewModel(
                    classroomId, taskId
                )
            }

        }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)

        }
    }


}