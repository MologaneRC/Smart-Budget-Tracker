package com.example.smartbudgettrack.data

import com.example.smartbudgettrack.model.UserStats

class UserStatsRepository(private val userStatsDao: UserStatsDao) {

    suspend fun addPoints(pointsToAdd: Int) {
        var stats = userStatsDao.getUserStats()
        if (stats == null) {
            stats = UserStats(id = 1, points = 0, level = 1)  // id = 1 to match DAO query
        }

        // Increment points
        val updatedPoints = stats.points + pointsToAdd

        // Leveling logic: every 100 points = level up
        val newLevel = (updatedPoints / 100) + 1

        // Create updated stats object
        val updatedStats = stats.copy(
            points = updatedPoints,
            level = if (newLevel > stats.level) newLevel else stats.level
        )

        // Insert or replace the updated stats
        userStatsDao.insertUserStats(updatedStats)
    }
}
