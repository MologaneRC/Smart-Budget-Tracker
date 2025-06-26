package com.example.smartbudgettrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smartbudgettrack.model.Transaction

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    suspend fun getAllTransactions(): List<Transaction>
}
