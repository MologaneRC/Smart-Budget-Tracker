package com.example.smartbudgettrack.data  // <- Change package to 'data' to match import in SignupActivity.kt

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartbudgettrack.model.Transaction
import com.example.smartbudgettrack.model.Budget
import com.example.smartbudgettrack.model.UserStats
import com.example.smartbudgettrack.data.TransactionDao
import com.example.smartbudgettrack.data.BudgetDao
import com.example.smartbudgettrack.data.UserStatsDao

@Database(entities = [Transaction::class, Budget::class, UserStats::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun userStatsDao(): UserStatsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_budget_db"
                )
                    // Add fallbackToDestructiveMigration if you want to reset db on schema changes during development
                    //.fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
