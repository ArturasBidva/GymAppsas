package com.example.gymappsas.ui.screens.workout

import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.util.UiText

data class WorkoutUiState(
    val isLoading: Boolean = true,
    val workouts: List<Workout> = listOf(),
    var workoutInfo: Workout? = null,
    val uiText: UiText? = null,
    val workout: Workout? = null,
    val isCreated: Boolean = false,
    val exerciseWorkouts: List<ExerciseWorkout> = listOf(),
    val exerciseWorkout: ExerciseWorkout? = null,
    val selectedWorkout: Workout? = null,
    var isDialogOpen: Boolean = false,
    var exercises : List<Exercise> = listOf(),
    val isSearching : Boolean = false,
    val searchText : String = "",
    val filteredWorkouts : List<Workout> = listOf(),
    val profile: Profile? = null,
)
