package com.example.gymappsas.ui.screens.workoutprep

import com.example.gymappsas.data.db.models.workouts.Workout
import kotlin.math.roundToInt

data class WorkoutPreparationUiState(
    val workouts: List<Workout> = listOf(),
    val selectedWorkout: Workout? = null,
    val isSelectWeightDialogOpen: Boolean = false,
    val selectedWeight: String = "5",
    val selectedExerciseWorkoutId: Long? = null,
    val progress: Int = 0,
    val selectedMethod: TrainingMethod = TrainingMethod.CONSTANT,
    val restTime: Int = 0,
    val trainingMethods: List<TrainingMethod> = TrainingMethod.entries,
    val currentStep: Int = 1,
    val customRestTime: String = "",
    val restTimeOptions: List<RestTimeOption> = restTimeOptionss,
    val workoutSaved: Boolean = false,
    val workoutVariantName: String = ""
)

val restTimeOptionss = listOf(
    RestTimeOption(60, "60 seconds", "Short, for endurance or fat loss"),
    RestTimeOption(90, "90 seconds", "Balanced"),
    RestTimeOption(120, "120 seconds", "For strength or heavy training"),
    RestTimeOption(-1, "Custom", "Set your own rest time")
)

enum class TrainingMethod(
    val displayName: String,
    val description: String,
    val weightStrategy: (Double) -> List<Double>,
    val reps: List<Any> // Int or String
) {
    CONSTANT(
        displayName = "Constant Weight",
        description = "Same weight across all sets",
        weightStrategy = { baseWeight ->
            List(4) { (baseWeight * 2).roundToInt() / 2.0 }
        },
        reps = listOf(10, 10, 10, 10)
    ),

    DROPSET(
        displayName = "Drop Set",
        description = "Decrease weight in each consecutive set",
        weightStrategy = { baseWeight ->
            weighted(baseWeight, listOf(100, 80, 60, 40))
        },
        reps = listOf(8, 10, 12, 15)
    ),

    FORCE(
        displayName = "Force Set",
        description = "Go beyond failure with help",
        weightStrategy = { baseWeight ->
            List(4) { (baseWeight * 2).roundToInt() / 2.0 }
        },
        reps = listOf("To Failure", "To Failure", "To Failure", "To Failure")
    ),

    PYRAMID(
        displayName = "Pyramid",
        description = "Increase weight each set, reduce reps",
        weightStrategy = { baseWeight ->
            weighted(baseWeight, listOf(70, 80, 90, 100))
        },
        reps = listOf(12, 10, 8, 6)
    ),

    REVERSE_PYRAMID(
        displayName = "Reverse Pyramid",
        description = "Start heavy and reduce weight",
        weightStrategy = { baseWeight ->
            weighted(baseWeight, listOf(100, 90, 80, 70))
        },
        reps = listOf(6, 8, 10, 12)
    );

    companion object {
        private const val MIN_WEIGHT = 2.5

        fun weighted(baseWeight: Double, percentages: List<Int>): List<Double> {
            return percentages.map {
                val weight = (baseWeight * it / 100).coerceAtLeast(MIN_WEIGHT)
                (weight * 2).roundToInt() / 2.0
            }
        }
    }
}

data class RestTimeOption(
    val value: Int,
    val label: String,
    val description: String
)
