package com.example.gymappsas.ui.screens.exercisesbyselectedcategory

import com.example.gymappsas.data.db.models.exercises.Exercise

data class ExerciseBySelectedCategoryUiState(
    val exerciseByCategory: Exercise? = null,
    val exercisesByCategory : List<Exercise> = listOf(),
    val isLoading : Boolean = false
)