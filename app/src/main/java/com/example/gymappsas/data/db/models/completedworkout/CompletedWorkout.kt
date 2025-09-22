package com.example.gymappsas.data.db.models.completedworkout

import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.db.models.workouts.WorkoutVariant
import java.time.LocalDate
import java.time.LocalDateTime

data class CompletedWorkout(
    val id: Long = 0L,
    val workout: Workout,
    val workoutVariant: WorkoutVariant? = null,
    val completedDate: LocalDate,
    val startedAt: LocalDateTime? = null,
    val completedAt: LocalDateTime? = null,
    val durationMinutes: Int? = null,
    val maxWeight: Float? = null,
    val notes: String? = null,
    val isCompleted: Boolean = false
)