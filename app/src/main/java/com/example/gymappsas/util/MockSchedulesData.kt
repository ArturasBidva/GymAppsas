package com.example.gymappsas.util

import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.data.db.models.workouts.Workout
import java.time.LocalDate
import java.time.LocalTime

object MockSchedulesData {
    private val mockWorkoutLocal = Workout(
        id = 1,
        title = "Upper Body Strength",
        description = "Focus on chest, shoulders, and triceps"
    )

    private val mockWorkoutCardio = Workout(
        id = 2,
        title = "HIIT Cardio",
        description = "High intensity interval training"
    )

    private val mockWorkoutLower = Workout(
        id = 3,
        title = "Lower Body Power",
        description = "Legs and glutes workout"
    )

    private val mockWorkoutCore = Workout(
        id = 4,
        title = "Core & Abs",
        description = "Core strengthening routine"
    )

    val mockSchedules: List<Schedule> = listOf(
        // Today's workouts
        Schedule(
            workout = mockWorkoutLocal,
            date = LocalDate.now(),
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(10, 30),
            color = 0xFF4CAF50.toInt(), // Green
            note = "Don't forget your towel and water bottle"
        ),
        Schedule(
            workout = mockWorkoutCardio,
            date = LocalDate.now(),
            startTime = LocalTime.of(18, 0),
            endTime = LocalTime.of(19, 0),
            color = 0xFFF44336.toInt(), // Red
            note = "High intensity session - bring extra water"
        ),

        // Tomorrow's workout
        Schedule(
            workout = mockWorkoutLower,
            date = LocalDate.now().plusDays(1),
            startTime = LocalTime.of(10, 0),
            endTime = LocalTime.of(11, 30),
            color = 0xFF2196F3.toInt(), // Blue
            note = "Focus on proper form, especially squats"
        ),

        // Day after tomorrow
        Schedule(
            workout = mockWorkoutCore,
            date = LocalDate.now().plusDays(2),
            startTime = LocalTime.of(8, 30),
            endTime = LocalTime.of(9, 30),
            color = 0xFFFF9800.toInt(), // Orange
            note = "Morning session - light breakfast recommended"
        ),

        // Next week workouts
        Schedule(
            workout = mockWorkoutLocal,
            date = LocalDate.now().plusDays(7),
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(10, 30),
            color = 0xFF4CAF50.toInt(), // Green
            note = "Week 2 progression - increase weights by 5%"
        ),
        Schedule(
            workout = mockWorkoutCardio,
            date = LocalDate.now().plusDays(9),
            startTime = LocalTime.of(17, 0),
            endTime = LocalTime.of(18, 0),
            color = 0xFFF44336.toInt(), // Red
            note = "Tabata style intervals today"
        ),
        Schedule(
            workout = mockWorkoutLower,
            date = LocalDate.now().plusDays(14),
            startTime = LocalTime.of(11, 0),
            endTime = LocalTime.of(12, 30),
            color = 0xFF2196F3.toInt(), // Blue
            note = "Add plyometric exercises for power development"
        )
    )
}