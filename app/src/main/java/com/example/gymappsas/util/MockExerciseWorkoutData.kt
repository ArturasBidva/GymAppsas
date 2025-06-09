package com.example.gymappsas.util

import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout

object MockExerciseWorkoutData {
    val mockExerciseWorkouts = listOf(
        ExerciseWorkout(
            id = 0,
            completedCount = 0,
            weight = 0,
            goal = 4,
            exercise = MockExerciseData.mockExercises[0]
        ),
        ExerciseWorkout(
            1,
            completedCount = 0,
            weight = 0,
            goal = 4,
            exercise = MockExerciseData.mockExercises[1])
    )
}