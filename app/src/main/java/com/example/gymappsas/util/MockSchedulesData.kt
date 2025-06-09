package com.example.gymappsas.util

import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.data.db.models.workouts.Workout
import java.time.LocalDate
import java.time.LocalTime

object MockSchedulesData {
    private val mockWorkoutLocal = Workout(
        id = 1,
        title = "Sample Workout",
        description = "This is a sample workout"
    )

    val mockSchedules: List<Schedule> = listOf(
        Schedule(
            workout = mockWorkoutLocal,
            date = LocalDate.now(),
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(10, 0),
            color = 0xFFAABBCC.toInt() // Example color
        ),
        Schedule(
            workout = mockWorkoutLocal,
            date = LocalDate.now().plusDays(1),
            startTime = LocalTime.of(10, 0),
            endTime = LocalTime.of(11, 0),
            color = 0xFFDDCCBB.toInt()
        )
    )
}