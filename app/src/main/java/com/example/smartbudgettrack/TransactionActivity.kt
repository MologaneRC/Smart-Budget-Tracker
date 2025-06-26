package com.example.smartbudgettrack

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartbudgettrack.data.AppDatabase
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartbudgettrack.data.TransactionDao
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TransactionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var backArrow: TextView

    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        // Initialize UI components
        recyclerView = findViewById(R.id.budgetRecyclerView)
        fabAdd = findViewById(R.id.fabAdd)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        backArrow = findViewById(R.id.backArrow)

        // Initialize Room database
        db = AppDatabase.getDatabase(this)
        transactionDao = db.transactionDao()

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TransactionAdapter(emptyList())
        recyclerView.adapter = adapter

        // Load real data from database
        loadTransactions()

        // FAB menu
        fabAdd.setOnClickListener {
            showFabOptions()
        }

        // Back button
        backArrow.setOnClickListener {
            finish()
        }

        // Bottom navigation
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_transactions -> true
                R.id.nav_stats -> {
                    startActivity(Intent(this, StatsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.nav_transactions
    }

    private fun loadTransactions() {
        lifecycleScope.launch {
            val transactions = transactionDao.getAllTransactions()
            adapter = TransactionAdapter(transactions)
            recyclerView.adapter = adapter
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
