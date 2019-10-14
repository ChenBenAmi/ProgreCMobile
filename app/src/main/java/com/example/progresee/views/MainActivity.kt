package com.example.progresee.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.progresee.R
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        setSupportActionBar(progresee_toolbar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progresee_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24px)
        navController = this.findNavController(R.id.myNavHostFragment)
        progresee_toolbar.elevation= 4.0F
        progresee_toolbar.setNavigationOnClickListener {
            when {
                navController.currentDestination?.id == R.id.homeFragment -> exitApp()
                else -> navController.navigateUp()
            }

        }
    }


    override fun onBackPressed() {
        when {
            navController.currentDestination?.id == R.id.homeFragment -> exitApp()
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
