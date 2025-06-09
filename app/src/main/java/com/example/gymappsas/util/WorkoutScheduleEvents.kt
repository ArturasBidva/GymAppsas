package com.example.gymappsas.util

sealed interface WorkoutScheduleEvents {
    data object None: WorkoutScheduleEvents

    data class Error(
        val error: UiText,
        val startTimeError: Boolean = false
    ) : WorkoutScheduleEvents
}