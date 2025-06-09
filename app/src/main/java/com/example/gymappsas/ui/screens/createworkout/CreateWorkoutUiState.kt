package com.example.gymappsas.ui.screens.createworkout

import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.workouts.Workout

data class CreateWorkoutUiState(
    val exerciseCategories : List<ExerciseCategory> = emptyList(),
    val isLoading : Boolean = false,
    val selectedCategories : List<String> = emptyList(),
    val exercisesBySelectedCategory : List<Exercise> = emptyList(),
    val workout : Workout? = null,
    val workoutTitle : String = "",
    val workoutDescription : String = "",
    val hasTitleError : Boolean = false,
    val hasDescriptionError : Boolean = false,
    val titleErrorMessage : String = "",
    val descriptionErrorMessage : String = "",
    val createdWorkoutId : Long? = null,
    var hasNavigated : Boolean = false
)
