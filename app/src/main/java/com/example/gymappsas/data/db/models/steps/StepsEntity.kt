package com.example.gymappsas.data.db.models.steps

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_steps")
data class StepsEntity(
    @PrimaryKey
    val date: String, // "yyyy-MM-dd" format
    val steps: Int,
    val timestamp: Long
)