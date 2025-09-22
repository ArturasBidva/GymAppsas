package com.example.gymappsas.ui.screens.workouthistory

import com.example.gymappsas.data.db.models.completedworkout.CompletedWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.db.models.workouts.WorkoutVariant

data class WorkoutHistoryUiState (
    val isLoading: Boolean = false,
    val workout: Workout? = null,
    val completedWorkouts: List<CompletedWorkout> = emptyList(),
    val workoutVariants: List<WorkoutVariant> = emptyList(),
    val error: String? = null
)
