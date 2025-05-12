package com.example.sbt

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddIncomeActivity : AppCompatActivity() {

    private lateinit var backArrow: TextView
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)

        backArrow = findViewById(R.id.backArrow)
        bottomNav = findViewById(R.id.bottomNavigationView)

        backArrow.setOnClickListener {
            finish() // Navigate back to dashboard
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_transactions -> {
                    startActivity(Intent(this, TransactionActivity::class.java))
                    true
                }
                R.id.nav_stats -> {
                    startActivity(Intent(this, StatsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val addButton = findViewById<Button>(R.id.buttonAddIncome)
        addButton.setOnClickListener {
            // Collect data and eventually save it (Room or ViewModel)
            val date = findViewById<EditText>(R.id.editTextDate).text.toString()
            val amount = findViewById<EditText>(R.id.editTextAmount).text.toString()
            val category = findViewById<EditText>(R.id.editTextCategory).text.toString()
            val description = findViewById<EditText>(R.id.editTextDescription).text.toString()

            Toast.makeText(this, "Income Added: $amount", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
