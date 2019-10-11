package com.example.progresee.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.progresee.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        when{
            navController.currentDestination?.id == R.id.loginFragment -> exitApp()
            else ->return navController.navigateUp()
        }
        return true
    }

    override fun onBackPressed() {
        when {
            navController.currentDestination?.id == R.id.loginFragment -> exitApp()
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
