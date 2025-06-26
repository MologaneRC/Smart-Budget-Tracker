package com.example.smartbudgettrack

import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartbudgettrack.data.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var totalBalance: TextView
    private lateinit var totalIncome: TextView
    private lateinit var totalExpense: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fabMain: FloatingActionButton
    private lateinit var pointsTextView: TextView
    private lateinit var levelTextView: TextView
    private lateinit var darkModeSwitch: Switch

    private lateinit var db: AppDatabase
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply dark mode setting before view loads
        val isDarkMode = getSharedPreferences("settings", MODE_PRIVATE).getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Redirect to login if not signed in
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Initialize views
        totalBalance = findViewById(R.id.textViewBalanceAmount)
        totalIncome = findViewById(R.id.textViewIncomeAmount)
        totalExpense = findViewById(R.id.textViewExpenseAmount)
        recyclerView = findViewById(R.id.recyclerView)
        bottomNav = findViewById(R.id.bottomNavigationView)
        fabMain = findViewById(R.id.fabAdd)
        pointsTextView = findViewById(R.id.textViewPoints)
        levelTextView = findViewById(R.id.textViewLevel)
        darkModeSwitch = findViewById(R.id.darkModeSwitch)

        db = AppDatabase.getDatabase(this)

        // Setup RecyclerView
        transactionAdapter = TransactionAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionAdapter

        // Load DB data
        loadUserStatsAndTransactions()

        // FAB options
        fabMain.setOnClickListener {
            showFabOptions()
        }

        // Bottom nav menu with calculator added
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> true
                R.id.nav_transactions -> {
                    startActivity(Intent(this, TransactionActivity::class.java))
                    true
                }
                R.id.nav_stats -> {
                    startActivity(Intent(this, StatsActivity::class.java))
                    true
                }
                R.id.nav_calculator -> {
                    startActivity(Intent(this, CalculatorActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Set switch state
        darkModeSwitch.isChecked = isDarkMode

        // Switch listener
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = getSharedPreferences("settings", MODE_PRIVATE).edit()
            editor.putBoolean("dark_mode", isChecked)
            editor.apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )

            recreate() // Restart activity to apply immediately
        }
    }

    private fun loadUserStatsAndTransactions() {
        lifecycleScope.launch(Dispatchers.IO) {
            var userStats = db.userStatsDao().getUserStats()
            if (userStats == null) {
                userStats = com.example.smartbudgettrack.model.UserStats(id = 1, points = 0, level = 1)
                db.userStatsDao().insertUserStats(userStats)
            }

            val transactions = db.transactionDao().getAllTransactions()

            withContext(Dispatchers.Main) {
                pointsTextView.text = "Points: ${userStats.points}"
                levelTextView.text = "Level: ${userStats.level}"

                transactionAdapter.setTransactions(transactions)

                val income = transactions.filter { it.isIncome }.sumOf { it.amount }
                val expense = transactions.filter { !it.isIncome }.sumOf { it.amount }
                val balance = income - expense

                totalIncome.text = "R${"%.2f".format(income)}"
                totalExpense.text = "R${"%.2f".format(expense)}"
                totalBalance.text = "R${"%.2f".format(balance)}"
            }
        }
    }

    private fun showFabOptions() {
        val options = arrayOf("Add Budget", "Add Income", "Add Expense")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> startActivity(Intent(this, AddBudgetActivity::class.java))
                1 -> startActivity(Intent(this, AddIncomeActivity::class.java))
                2 -> startActivity(Intent(this, AddExpenseActivity::class.java))
            }
        }
        builder.show()
    }
}
