package com.example.smartbudgettrack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey val id: Int = 1, // only one stats record
    var points: Int = 0,
    var level: Int = 1,
    var badge: String = "Beginner"
)
