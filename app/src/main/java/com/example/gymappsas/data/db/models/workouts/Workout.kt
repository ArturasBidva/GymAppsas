package com.example.gymappsas.data.db.models.workouts

import android.os.Parcelable
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.util.GetImagePath
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class Workout(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val category: WorkoutCategory? = null,
    val exerciseWorkouts: List<ExerciseWorkout> = listOf(),
    val trainingMethod: String? = null,
    val variants: List<WorkoutVariant>? = null,
    val isFavorite: Boolean = false,
) : Parcelable {

    val image: String
        get() {
            val firstExercise = exerciseWorkouts.firstOrNull()?.exercise ?: return ""
            return GetImagePath.getExerciseImagePath(
                category = firstExercise.primaryMuscles.first(),
                exerciseName = firstExercise.name,
                imageIndex = 0
            )
        }
    val workoutTime: Long
        get() = getTotalTimeMinutes()

    // Helper properties for variants
    val hasRecentActivity: Boolean
        get() = lastUsedVariant != null

    val lastUsedVariant: WorkoutVariant?
        get() = variants?.filter { it.lastUsedAt != null }
            ?.maxByOrNull { it.lastUsedAt!! }

    val favoriteVariant: WorkoutVariant?
        get() = variants?.filter { it.lastUsedAt != null }
            ?.maxByOrNull { it.lastUsedAt!! } ?: variants?.firstOrNull()

    fun doesMatchSearchQuery(query: String): Boolean {
        val lowerCaseQuery = query.lowercase(Locale.ROOT)
        val titleWords = title.lowercase(Locale.ROOT).split(" ")
        return titleWords.any { it.contains(lowerCaseQuery) }
    }

    private fun getTotalTimeMillis(): Long {
        return exerciseWorkouts.sumOf {
            (it.duration + it.breakTime) * it.goal
        }
    }

    private fun getTotalTimeMinutes(): Long {
        return getTotalTimeMillis() / 1000 / 60
    }

}

enum class WorkoutCategory(val displayName: String) {
    STRENGTH("Strength"),
    CARDIO("Cardio"),
    CORE("Core"),
    OTHER("Other")
}