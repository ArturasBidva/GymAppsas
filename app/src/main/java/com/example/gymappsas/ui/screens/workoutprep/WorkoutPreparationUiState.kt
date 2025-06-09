package com.example.gymappsas.ui.screens.workoutprep

import com.example.gymappsas.data.db.models.workouts.Workout

data class WorkoutPreparationUiState(
    val workouts : List<Workout> = listOf(),
    val selectedWorkout : Workout? = null,
    val isSelectWeightDialogOpen : Boolean = false,
    val selectedWeight : String = "5",
    val selectedExerciseWorkoutId : Long? = null,
    val progress : Int = 0
)
