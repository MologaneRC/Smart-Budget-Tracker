package com.example.smartbudgettrack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smartbudgettrack.data.AppDatabase
import com.example.smartbudgettrack.data.TransactionDao
import com.example.smartbudgettrack.data.UserStatsDao
import com.example.smartbudgettrack.model.UserStats
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class StatsActivity : AppCompatActivity() {

    private lateinit var incomeText: TextView
    private lateinit var expenseText: TextView
    private lateinit var remainingText: TextView
    private lateinit var pointsTextView: TextView
    private lateinit var levelTextView: TextView
    private lateinit var barChart: BarChart
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var userStatsDao: UserStatsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        // Link UI elements
        val labelsLayout = findViewById<LinearLayout>(R.id.labelsLayout)
        incomeText = labelsLayout.getChildAt(0) as TextView
        expenseText = labelsLayout.getChildAt(1) as TextView
        remainingText = labelsLayout.getChildAt(2) as TextView
        pointsTextView = findViewById(R.id.textViewPoints)
        levelTextView = findViewById(R.id.textViewLevel)
        barChart = findViewById(R.id.barChart)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Init Room DB and DAOs
        db = AppDatabase.getDatabase(this)
        transactionDao = db.transactionDao()
        userStatsDao = db.userStatsDao()

        // Load data
        loadStats()
        loadUserStats()

        // Bottom nav
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
                R.id.nav_stats -> true
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.nav_stats
    }

    private fun loadStats() {
        lifecycleScope.launch {
            val transactions = transactionDao.getAllTransactions()

            var totalIncome = 0f
            var totalExpense = 0f

            for (transaction in transactions) {
                val amount = transaction.amount.toFloat()
                if (transaction.isIncome) {
                    totalIncome += amount
                } else {
                    totalExpense += amount
                }
            }

            val remaining = totalIncome - totalExpense

            // Update text labels
            incomeText.text = "Income: R${String.format("%.2f", totalIncome)}"
            expenseText.text = "Expenses: R${String.format("%.2f", totalExpense)}"
            remainingText.text = "Remaining Money: R${String.format("%.2f", remaining)}"

            // Populate Bar Chart
            val entries = listOf(
                BarEntry(0f, totalIncome),
                BarEntry(1f, totalExpense),
                BarEntry(2f, remaining)
            )

            val dataSet = BarDataSet(entries, "Financial Stats").apply {
                colors = listOf(
                    Color.parseColor("#00BCD4"), // Income - light blue
                    Color.parseColor("#03A9F4"), // Expense - deeper blue
                    Color.parseColor("#0288D1")  // Remaining - darkest blue
                )
                valueTextSize = 14f
                valueTextColor = Color.BLACK
            }

            val data = BarData(dataSet)
            barChart.data = data

            barChart.xAxis.apply {
                granularity = 1f
                setDrawGridLines(false)
                setDrawLabels(false)
            }

            barChart.axisLeft.axisMinimum = 0f
            barChart.axisRight.isEnabled = false
            barChart.description = Description().apply { text = "" }
            barChart.legend.isEnabled = false
            barChart.setFitBars(true)
            barChart.animateY(800)
            barChart.invalidate()
        }
    }

    private fun loadUserStats() {
        lifecycleScope.launch {
            val stats: UserStats? = userStatsDao.getUserStats()
            stats?.let {
                pointsTextView.text = "Points: ${it.points}"
                levelTextView.text = "Level: ${it.level}"
            } ?: run {
                pointsTextView.text = "Points: 0"
                levelTextView.text = "Level: 1"
            }
        }
    }
}
