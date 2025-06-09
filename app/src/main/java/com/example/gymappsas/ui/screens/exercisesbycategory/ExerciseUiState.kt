package com.example.gymappsas.ui.screens.exercisesbycategory


import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.util.UiText

data class ExerciseUiState(
    val selectedString: String? = null,
    val exerciseCategories: List<ExerciseCategory> = emptyList(),
    val exercises: List<Exercise> = emptyList(),
    val isLoading: Boolean = true,
    val selectedExercise: Exercise? = null,
    val uiText: UiText? = null,
    val isError: Boolean = false,
    val selectionExpanded: Boolean = false,
)