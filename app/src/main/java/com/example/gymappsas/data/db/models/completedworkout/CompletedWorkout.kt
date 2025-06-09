package com.example.gymappsas.data.db.models.completedworkout

import com.example.gymappsas.data.db.models.workouts.Workout
import java.time.LocalDate

data class CompletedWorkout(
    val id: Long = 0L,
    val workout : Workout,
    val completedDate : LocalDate,

    )