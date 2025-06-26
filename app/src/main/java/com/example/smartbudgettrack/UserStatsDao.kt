package com.example.smartbudgettrack.data

import androidx.room.*
import com.example.smartbudgettrack.model.UserStats

@Dao
interface UserStatsDao {

    // Correct: get the only UserStats record with id = 1
    @Query("SELECT * FROM user_stats WHERE id = 1 LIMIT 1")
    suspend fun getUserStats(): UserStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStats(stats: UserStats)

    @Update
    suspend fun updateUserStats(stats: UserStats)
}
