package com.example.smartbudgettrack

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartbudgettrack.data.AppDatabase
import com.example.smartbudgettrack.model.Transaction
import com.example.smartbudgettrack.model.UserStats
import com.example.smartbudgettrack.data.TransactionDao
import com.example.smartbudgettrack.data.UserStatsDao
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var dateEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var categoryEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var addExpenseButton: Button
    private lateinit var backArrow: TextView
    private lateinit var bottomNav: BottomNavigationView

    private lateinit var transactionDao: TransactionDao
    private lateinit var userStatsDao: UserStatsDao
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Initialize views
        dateEditText = findViewById(R.id.editTextDate)
        amountEditText = findViewById(R.id.editTextAmount)
        categoryEditText = findViewById(R.id.editTextCategory)
        descriptionEditText = findViewById(R.id.editTextDescription)
        addExpenseButton = findViewById(R.id.buttonAddExpense)
        backArrow = findViewById(R.id.backArrow)
        bottomNav = findViewById(R.id.bottomNavigationView)

        // Initialize FirebaseAuth and DAOs
        auth = FirebaseAuth.getInstance()
        val db = AppDatabase.getDatabase(this)
        transactionDao = db.transactionDao()
        userStatsDao = db.userStatsDao()

        addExpenseButton.setOnClickListener {
            val name = categoryEditText.text.toString().trim()
            val amountText = amountEditText.text.toString().trim()
            val date = dateEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (name.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount: Double = try {
                amountText.toDouble()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transaction = Transaction(
                name = name,
                amount = amount,
                date = date,
                description = description,
                isIncome = false // Expense
            )

            CoroutineScope(Dispatchers.IO).launch {
                transactionDao.insert(transaction)

                // Get current user ID
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    // Fetch user stats by userId, or create new if null
                    val stats = userStatsDao.getUserStats() ?: UserStats(id = 1, points = 0, level = 1)
                    val pointsToAdd = (amount / 20).toInt().coerceAtLeast(5) // Example: 1 point per 20 units spent, min 5 points
                    val newPoints = stats.points + pointsToAdd
                    val newLevel = (newPoints / 100) + 1
                    val updatedStats = stats.copy(points = newPoints, level = newLevel)

                    userStatsDao.insertUserStats(updatedStats)
                }

                runOnUiThread {
                    Toast.makeText(this@AddExpenseActivity, "Expense added! +${(amount / 20).toInt().coerceAtLeast(5)} points", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        backArrow.setOnClickListener { finish() }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    finish()
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
