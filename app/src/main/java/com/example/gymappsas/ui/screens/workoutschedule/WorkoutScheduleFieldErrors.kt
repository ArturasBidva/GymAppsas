package com.example.gymappsas.ui.screens.workoutschedule

data class WorkoutScheduleFieldErrors(
    val startTimeError: Boolean = false,
    val endTimeError: Boolean = false,
    val workoutSelectError: Boolean = false
) {
    fun hasErrors(): Boolean {
        return startTimeError || endTimeError || workoutSelectError
    }
}