package com.example.gymappsas.ui.screens.createexercise

import com.example.gymappsas.data.db.models.exercises.Exercise


data class CreateExerciseUiState(
    val exerciseCategories : List<String> = emptyList(),
    val categoryVisibility : Boolean = false,
    val isLoading : Boolean = true,
    val selectedCategory : String? = null,
    val exercise : Exercise? = null,
    val exerciseTitle : String = "",
    val exerciseDescription : String = "",
    val fieldErrors: CreateExerciseFieldErrors = CreateExerciseFieldErrors()
)