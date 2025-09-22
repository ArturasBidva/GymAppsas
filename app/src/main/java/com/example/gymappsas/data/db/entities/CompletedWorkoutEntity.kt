package com.example.gymappsas.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "completed_workouts")
data class CompletedWorkoutEntity(
    @PrimaryKey
    val id: Long = 0L,
    val workoutId: Long,
    val completedDate: LocalDate,
    val workoutTitle: String,
    val durationMinutes: Int? = null, // Workout duration in minutes
    val maxWeight: Float? = null, // Maximum weight used in kg
    val notes: String? = null // Optional notes about the workout
)