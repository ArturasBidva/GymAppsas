package com.example.gymappsas.ui.screens.workouthistory

import com.example.gymappsas.data.db.models.completedworkout.CompletedWorkout


data class WorkoutHistoryUiState (
    val isLoading: Boolean = false,
    val workoutHistory: List<CompletedWorkout> = emptyList()
) {

}
