package com.example.gymappsas

import kotlinx.serialization.Serializable

@Serializable
object MainGraph

@Serializable
object MainScreen

@Serializable
object ProfileScreen

@Serializable
object SettingsScreen

@Serializable
object WorkoutScreen

@Serializable
object ExerciseScreen

@Serializable
object WorkoutScheduleScreen

@Serializable
data class ExerciseBySelectedCategoryScreen(val exerciseCategory: String)

@Serializable
data class ExerciseDetailsScreen(val exerciseId: Long)

@Serializable
object CreateWorkoutScreen

@Serializable
data class OnGoingWorkoutScreen(val workoutId: Long)

@Serializable
object ProfileSetupScreen

@Serializable
data class WorkoutPreparationScreen(val workoutId: Long)

@Serializable
data class WorkoutVariantsScreen(val workoutId: Long)
