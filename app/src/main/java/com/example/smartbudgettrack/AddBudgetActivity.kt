package com.example.smartbudgettrack

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartbudgettrack.data.AppDatabase
import com.example.smartbudgettrack.model.Budget
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class AddBudgetActivity : AppCompatActivity() {

    private lateinit var editTextCategory: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonAddBudget: Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var backArrow: TextView

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_budget)

        // Init DB
        db = AppDatabase.getDatabase(this)

        // UI bindings
        editTextCategory = findViewById(R.id.editTextCategory)
        editTextAmount = findViewById(R.id.editTextAmount)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonAddBudget = findViewById(R.id.buttonAddBudget)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        backArrow = findViewById(R.id.backArrow)

        // Back button
        backArrow.setOnClickListener {
            finish()
        }

        // Add budget on click
        buttonAddBudget.setOnClickListener {
            val category = editTextCategory.text.toString().trim()
            val amountText = editTextAmount.text.toString().trim()
            val description = editTextDescription.text.toString().trim()

            if (category.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val budget = Budget(category = category, amount = amount, description = description)

            lifecycleScope.launch {
                db.budgetDao().insert(budget)
                runOnUiThread {
                    Toast.makeText(this@AddBudgetActivity, "Budget added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        // Bottom navigation
        bottomNavigationView.setOnItemSelectedListener {
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
    }
}
