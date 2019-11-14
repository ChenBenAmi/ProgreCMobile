package com.app.progrec.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.app.progrec.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        setSupportActionBar(progresee_toolbar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = this.findNavController(R.id.myNavHostFragment)
        progresee_toolbar.elevation = 4.0F
    }


    override fun onBackPressed() {
        when {
            navController.currentDestination?.id == R.id.homeFragment -> exitApp()
            navController.currentDestination?.id == R.id.classroomFragment -> exitApp()
            else -> super.onBackPressed()
        }
    }

    private fun exitApp() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.exit)
        builder.setMessage(R.string.exit_app)
        builder.setPositiveButton("Yes") { dialog, which ->
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
