package com.example.gymappsas.data.db.models.workouts

import android.os.Parcelable
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.util.TimerUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutVariant(
    val id: Long,
    val name: String,
    val trainingMethod: String,
    val restTimeSeconds: Int,
    val lastUsedAt: Long? = null,
    val createdAt: String = TimerUtil.getFormattedTime(System.currentTimeMillis()),
    val estimatedDuration: Long = 0,
    val exercises: List<ExerciseWorkout> = emptyList(),
    val isFavourite: Boolean = false,
    val description: String? = null
) : Parcelable {
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