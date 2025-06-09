package com.example.gymappsas.data.db.models.workouts

import android.os.Parcelable
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class Workout(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val exerciseWorkouts: List<ExerciseWorkout> = listOf()
) : Parcelable
{
    fun doesMatchSearchQuery(query: String): Boolean {
        val lowerCaseQuery = query.lowercase(Locale.ROOT)
        val titleWords = title.lowercase(Locale.ROOT).split(" ")
        return titleWords.any { it.contains(lowerCaseQuery) }
    }
}
