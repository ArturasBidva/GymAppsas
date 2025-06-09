package com.example.gymappsas.ui.screens.exercisedetails

import com.example.gymappsas.data.db.models.exercises.Exercise

data class ExerciseDetailsUiState(
    val selectedExercise : Exercise? = null,
    val isLoading : Boolean = false
)