package com.example.sbt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch) // Make sure your XML file is named activity_launch.xml

        // Delay for 2.5 seconds before moving to the next screen (e.g., LoginActivity)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finish the splash screen so user can't go back to it
        }, 2500)
    }
}