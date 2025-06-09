package com.example.gymappsas.ui.screens.addexercisetoworkout

import com.example.gymappsas.data.db.models.exercises.Exercise

data class AddExerciseToWorkoutUiState(
    val isLoading : Boolean = false,
    val exercises : List<Exercise> = listOf(),
    val selectedExercises : List<Exercise> = emptyList(),
)