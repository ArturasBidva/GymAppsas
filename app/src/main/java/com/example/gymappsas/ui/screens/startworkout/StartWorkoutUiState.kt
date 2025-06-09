package com.example.gymappsas.ui.screens.startworkout

import com.example.gymappsas.data.db.models.workouts.Workout

data class StartWorkoutUiState(
    val selectedWorkout: Workout? = null,
    val completedWorkout : Workout? = null,
    val isWorkoutCompleted : Boolean = false,
    val weeklyWorkoutCounter: Int = 0
) {
}