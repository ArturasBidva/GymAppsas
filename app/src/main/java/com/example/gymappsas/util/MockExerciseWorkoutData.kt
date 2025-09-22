package com.example.gymappsas.util

import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout

object MockExerciseWorkoutData {
    val mockExerciseWorkouts = listOf(
        ExerciseWorkout(
            id = 0,
            completedCount = 0,
            maxWeight = 25F,
            goal = 4,
            weights = listOf(100F, 120F, 140F, 160F),
            exercise = MockExerciseData.mockExercises[0]
        ),
        ExerciseWorkout(
            1,
            completedCount = 0,
            maxWeight = 0F,
            goal = 4,
            exercise = MockExerciseData.mockExercises[1]),
        ExerciseWorkout(
            3,
            completedCount = 0,
            maxWeight = 0F,
            goal = 4,
            exercise = MockExerciseData.mockExercises[2]),
        ExerciseWorkout(
            4,
            completedCount = 0,
            maxWeight = 0F,
            goal = 4,
            exercise = MockExerciseData.mockExercises[3]), ExerciseWorkout(
            5,
            completedCount = 0,
            maxWeight = 0F,
            goal = 4,
            exercise = MockExerciseData.mockExercises[4]),
    )
}