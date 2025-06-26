package com.example.smartbudgettrack

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartbudgettrack.data.AppDatabase
import com.example.smartbudgettrack.data.TransactionDao
import com.example.smartbudgettrack.data.UserStatsDao
import com.example.smartbudgettrack.model.Transaction
import com.example.smartbudgettrack.model.UserStats
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddIncomeActivity : AppCompatActivity() {

    private lateinit var dateEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addIncomeButton: Button
    private lateinit var backArrow: TextView
    private lateinit var bottomNav: BottomNavigationView

    private lateinit var transactionDao: TransactionDao
    private lateinit var userStatsDao: UserStatsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)

        // Initialize views
        dateEditText = findViewById(R.id.editTextDate)
        amountEditText = findViewById(R.id.editTextAmount)
        categoryEditText = findViewById(R.id.editTextCategory)
        descriptionEditText = findViewById(R.id.editTextDescription)
        addIncomeButton = findViewById(R.id.buttonAddIncome)
        backArrow = findViewById(R.id.backArrow)
        bottomNav = findViewById(R.id.bottomNavigationView)

        // Init Room DAOs
        val db = AppDatabase.getDatabase(this)
        transactionDao = db.transactionDao()
        userStatsDao = db.userStatsDao()

        addIncomeButton.setOnClickListener {
            val name = categoryEditText.text.toString().trim()
            val amountText = amountEditText.text.toString().trim()
            val date = dateEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (name.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transaction = Transaction(
                name = name,
                amount = amount,
                date = date,
                description = description,
                isIncome = true
            )

            CoroutineScope(Dispatchers.IO).launch {
                transactionDao.insert(transaction)

                val stats = userStatsDao.getUserStats() ?: UserStats(id = 1, points = 0, level = 1)
                val newPoints = stats.points + 10
                val newLevel = (newPoints / 100) + 1
                val newBadge = when {
                    newPoints >= 500 -> "Expert"
                    newPoints >= 300 -> "Advanced"
                    newPoints >= 100 -> "Intermediate"
                    else -> "Beginner"
                }

                val updatedStats = stats.copy(points = newPoints, level = newLevel, badge = newBadge)
                userStatsDao.insertUserStats(updatedStats)

                runOnUiThread {
                    Toast.makeText(this@AddIncomeActivity, "Income added! +10 points", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        backArrow.setOnClickListener { finish() }

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
    }
}
