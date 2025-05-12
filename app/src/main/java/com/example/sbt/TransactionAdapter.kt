package com.example.sbt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val amount: TextView = itemView.findViewById(R.id.amountTextView)
        val date: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.name.text = transaction.name
        holder.amount.text = transaction.amount
        holder.date.text = transaction.date
        holder.amount.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (transaction.isIncome) R.color.green else R.color.red
            )
        )
    }

    override fun getItemCount(): Int = transactions.size
}
