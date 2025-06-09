package com.example.gymappsas.ui.screens.workoutschedule

import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.data.db.models.workouts.Workout
import java.time.LocalDate
import java.time.LocalTime

data class WorkoutScheduleUiState(
    val selectedWorkout: Workout = Workout(),
    val schedule: Schedule = Schedule(),
    val schedules: List<Schedule> = listOf(),
    val isDialogVisible: Boolean = false,
    val isCalendarDialogVisible: Boolean = false,
    val timeSelectionDialogType: TimeSelectionDialogType? = null,
    val selectedCalendarDate: LocalDate? = null,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val fieldErrors: WorkoutScheduleFieldErrors = WorkoutScheduleFieldErrors()
) {
    fun getTimeSelectionTime(): LocalTime? {
        return when (timeSelectionDialogType) {
            is TimeSelectionDialogType.StartTime -> schedule.startTime
            is TimeSelectionDialogType.EndTime -> schedule.endTime

            else -> null
        }
    }
}