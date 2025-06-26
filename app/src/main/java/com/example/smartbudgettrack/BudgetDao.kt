package com.example.smartbudgettrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smartbudgettrack.model.Budget

@Dao
interface BudgetDao {
    @Insert
    suspend fun insert(budget: Budget)

    @Query("SELECT * FROM budgets")
    suspend fun getAllBudgets(): List<Budget>
}
