package com.app.progrec

import android.app.Application
import com.app.progrec.data.AppRepository
import com.app.progrec.data.network.ApiService
import com.app.progrec.viewmodels.*
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

            single { ApiService() }
            single { AppRepository(get()) }

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
            viewModel { (appRepository: AppRepository, classroomId: String, taskId: String) ->
                TaskDetailsViewModel(
                    appRepository,
                    classroomId, taskId
                )
            }
            viewModel { (appRepository: AppRepository, classroomId: String) ->
                UserViewModel(
                    appRepository,
                    classroomId
                )
            }

            viewModel { (appRepository: AppRepository, classroomId: String, taskId: String) ->
                CreateTaskViewModel(
                    appRepository,
                    classroomId, taskId
                )
            }

            viewModel { (appRepository: AppRepository, classroomId: String, exerciseId: String) ->
                UsersFinishedViewModel(
                    appRepository,
                    classroomId, exerciseId
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