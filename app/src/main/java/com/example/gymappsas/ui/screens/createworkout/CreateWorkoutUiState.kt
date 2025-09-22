package com.example.gymappsas.ui.screens.createworkout

import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.db.models.workouts.WorkoutCategory

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
    var hasNavigated : Boolean = false,
    val createWorkoutStep : CreateWorkoutStep = CreateWorkoutStep.WORKOUTDETAILS,
    val exercises : List<Exercise> = emptyList(),
    val selectedExercises : List<Exercise> = emptyList(),
    val selectedCategory : WorkoutCategory? = null
)
sealed class CreateWorkoutStep {
    data object WORKOUTDETAILS : CreateWorkoutStep()
    data object ADDEXERCISES : CreateWorkoutStep()
    data object COMPLETE : CreateWorkoutStep()

}