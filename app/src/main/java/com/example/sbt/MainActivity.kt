package com.example.sbt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var totalBalance: TextView
    private lateinit var totalIncome: TextView
    private lateinit var totalExpense: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var fabMain: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // MUST come before findViewById

        recyclerView = findViewById(R.id.recyclerView) // make sure ID matches your XML
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TransactionAdapter(sampleTransactionList())

        totalBalance = findViewById(R.id.textViewBalanceAmount)
        totalIncome = findViewById(R.id.textViewIncomeAmount)
        totalExpense = findViewById(R.id.textViewExpenseAmount)
        bottomNav = findViewById(R.id.bottomNavigationView)
        fabMain = findViewById(R.id.fabAdd)



        // Sample transaction setup
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.recyclerView)


        // FAB click event
        fabMain.setOnClickListener {
            showFabOptions()
        }

        // Bottom nav listener
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
                else -> false
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

    private fun sampleTransactionList(): List<Transaction> {
        return listOf(
            Transaction("Salary", "R15000", "2025-05-01", true),
            Transaction("Household", "R5500", "2025-05-03", false),
            Transaction("Groceries", "R2500", "2025-05-04", false),
            Transaction("Transport", "R500", "2025-05-05", false)
        )
    }
}
