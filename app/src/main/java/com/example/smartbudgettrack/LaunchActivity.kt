package com.example.smartbudgettrack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        // Delay for 2 seconds then move to LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@LaunchActivity, LoginActivity::class.java))
            finish() // Close this activity
        }, 2000) // 2000 milliseconds = 2 seconds
    }
}
