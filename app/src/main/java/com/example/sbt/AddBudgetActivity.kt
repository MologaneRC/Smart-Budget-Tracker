package com.example.sbt

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AddBudgetActivity : AppCompatActivity() {

    private lateinit var editTextCategory: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonAddBudget: Button
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var backArrow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_budget)

        editTextCategory = findViewById(R.id.editTextCategory)
        editTextAmount = findViewById(R.id.editTextAmount)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonAddBudget = findViewById(R.id.buttonAddBudget)
        bottomNav = findViewById(R.id.bottomNavigationView)
        backArrow = findViewById(R.id.backArrow)

        buttonAddBudget.setOnClickListener {
            val category = editTextCategory.text.toString()
            val amount = editTextAmount.text.toString()
            val description = editTextDescription.text.toString()

            if (category.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Budget added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        backArrow.setOnClickListener {
            finish()
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    finish()
                    true
                }
                R.id.nav_transactions -> {
                    // Optional: open TransactionActivity
                    true
                }
                R.id.nav_stats -> {
                    // Optional: open StatsActivity
                    true
                }
                else -> false
            }
        }
    }
}
