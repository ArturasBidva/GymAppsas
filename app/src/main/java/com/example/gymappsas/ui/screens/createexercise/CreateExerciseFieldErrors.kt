package com.example.gymappsas.ui.screens.createexercise

data class CreateExerciseFieldErrors(
    val titleError : Boolean = false,
    val descriptionError : Boolean = false,
    val exerciseCategoryError : Boolean = false
) {
    fun hasErrors(): Boolean {
        return titleError || descriptionError || exerciseCategoryError
    }
}