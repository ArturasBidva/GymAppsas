package com.example.gymappsas.util

import com.example.gymappsas.data.db.models.workouts.Workout

object MockWorkoutData {
    val mockWorkout = Workout(
            id = 0,
            title = "Legs day",
            description =  "This is random description",
            exerciseWorkouts = MockExerciseWorkoutData.mockExerciseWorkouts)

    val mockWorkouts = listOf(Workout(
        id = 0,
        title = "Legs day",
        description =  "This is random description",
        exerciseWorkouts = MockExerciseWorkoutData.mockExerciseWorkouts)
        ,Workout(
        id = 1,
        title = "Legs day",
        description =  "This is random description",
        exerciseWorkouts = MockExerciseWorkoutData.mockExerciseWorkouts))
}
