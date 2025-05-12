package com.example.sbt

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class TransactionActivity : AppCompatActivity() {

    private lateinit var budgetRecyclerView: RecyclerView
    private lateinit var adapter: BudgetAdapter
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var backArrow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        // Set up RecyclerView
        budgetRecyclerView = findViewById(R.id.budgetRecyclerView)
        budgetRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sample budget data
        val budgetItems = listOf(
            BudgetItem("Total Budget", 15000.0, 9500.0),
            BudgetItem("Household", 6500.0, 5500.0),
            BudgetItem("Grocery", 2500.0, 2500.0),
            BudgetItem("Transport", 1000.0, 500.0)
        )

        adapter = BudgetAdapter(budgetItems)
        budgetRecyclerView.adapter = adapter

        // Back arrow click
        backArrow = findViewById(R.id.backArrow)
        backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Bottom navigation
        bottomNav = findViewById(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.nav_transactions

        bottomNav.setOnItemSelectedListener {
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
    }
}
