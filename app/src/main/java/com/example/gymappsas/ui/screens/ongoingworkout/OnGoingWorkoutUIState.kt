package com.example.gymappsas.ui.screens.ongoingworkout

import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.Workout

data class OnGoingWorkoutUIState(
    val onGoingWorkout: Workout? = null,
    val currentSet: Int = 0,
    val totalSets: Int = 0,
    val currentExercise : ExerciseWorkout? = null,
    val progress: Float = 0f,
    val workoutCompleted : Boolean = false,
    val nextExercise: ExerciseWorkout? = null,
    val isPaused: Boolean = false,
    val isTimerRunning: Boolean = false,
    val isExpanded :Boolean = false
)