package com.example.gymappsas.ui.screens.ongoingworkout

sealed class WorkoutPhase {
    object Exercise : WorkoutPhase()
    object Rest : WorkoutPhase()
    object WorkoutCompleted : WorkoutPhase()
    object ExerciseCompleted: WorkoutPhase()
    data class SetCompleted(val currentSet: Int, val totalSets: Int) : WorkoutPhase()
}