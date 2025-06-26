package com.example.smartbudgettrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BudgetAdapter(private val items: List<BudgetItem>) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    class BudgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTextView: TextView = view.findViewById(R.id.textCategory)
        val expenseTextView: TextView = view.findViewById(R.id.textExpenseAmount)
        val budgetTextView: TextView = view.findViewById(R.id.textBudgetAmount)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_budget_progress, parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = items[position]
        holder.categoryTextView.text = item.category
        holder.expenseTextView.text = "R${item.expenseAmount}"
        holder.budgetTextView.text = "R${item.budgetAmount}"

        // Calculate progress percent (expense/budget * 100)
        val progressPercent = if (item.budgetAmount > 0) {
            ((item.expenseAmount / item.budgetAmount) * 100).toInt().coerceIn(0, 100)
        } else {
            0
        }
        holder.progressBar.progress = progressPercent
    }

    override fun getItemCount(): Int = items.size
}
