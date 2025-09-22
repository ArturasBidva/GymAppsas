package com.example.gymappsas.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_variants")
data class WorkoutVariantEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val workoutId: Long,
    val name: String,
    val trainingMethod: String,
    val restTimeSeconds: Int,
    val exerciseWorkoutsJson: String, // JSON-encoded list
    val createdAt: Long, // epoch millis
    val lastUsedAt: Long? = null, // epoch millis when last used/started
    val notes: String? = null
) {
    fun isRecentlyUsed(): Boolean {
        return lastUsedAt != null && (System.currentTimeMillis() - lastUsedAt) < 7 * 24 * 60 * 60 * 1000L // 7 days
    }

    fun getDaysAgo(): String? {
        return lastUsedAt?.let {
            val daysAgo = (System.currentTimeMillis() - it) / (24 * 60 * 60 * 1000L)
            when {
                daysAgo == 0L -> "Today"
                daysAgo == 1L -> "Yesterday"
                daysAgo < 7L -> "$daysAgo days ago"
                else -> "${daysAgo / 7} weeks ago"
            }
        }
    }
}
