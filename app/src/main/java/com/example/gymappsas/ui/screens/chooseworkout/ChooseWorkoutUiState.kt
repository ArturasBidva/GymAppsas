package com.example.gymappsas.ui.screens.chooseworkout

import com.example.gymappsas.data.db.models.workouts.Workout


data class ChooseWorkoutUiState(
    val chosenWorkout: Workout? = null,
    val workouts: List<Workout> = emptyList(),
    val isLoading : Boolean = false
) {
}