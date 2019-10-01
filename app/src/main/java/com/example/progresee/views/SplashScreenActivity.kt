package com.example.progresee.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.progresee.R
import com.example.progresee.viewmodels.SplashState
import com.example.progresee.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }


        splashViewModel.liveData.observe(this, Observer {
            when (it) {
                is SplashState.MainActivity -> {
                    goToMainActivity()
                }
            }
        })
    }

    private fun goToMainActivity() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}
