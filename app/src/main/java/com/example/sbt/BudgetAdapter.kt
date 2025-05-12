package com.example.sbt

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BudgetAdapter(private val budgetList: List<BudgetItem>) :
    RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory: TextView = itemView.findViewById(R.id.textCategory)
        val textBudget: TextView = itemView.findViewById(R.id.textBudgetAmount)
        val textExpense: TextView = itemView.findViewById(R.id.textExpenseAmount)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_budget_progress, parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = budgetList[position]
        holder.textCategory.text = item.category
        holder.textBudget.text = "R${item.budgetAmount}"
        holder.textExpense.text = "R${item.expenseAmount}"

        val progress = if (item.budgetAmount == 0.0) 0 else
            ((item.expenseAmount / item.budgetAmount) * 100).toInt()

        holder.progressBar.progress = progress
        holder.progressBar.progressDrawable.setTint(
            if (item.expenseAmount > item.budgetAmount) Color.RED else Color.BLUE
        )
    }

    override fun getItemCount(): Int = budgetList.size
}
