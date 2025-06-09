package com.example.gymappsas.data.db.models.schedules

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.gymappsas.data.db.models.workouts.Workout
import java.time.LocalDate
import java.time.LocalTime

data class Schedule(
    var workout: Workout = Workout(),
    var date: LocalDate = LocalDate.now(),
    var startTime: LocalTime? = null,
    var endTime: LocalTime? = null,
    var color: Int = Color.Blue.toArgb(),
    var note: String = ""
)