package com.example.gymappsas.util

import com.example.gymappsas.data.db.models.exercises.Exercise

object MockExerciseData {
    val mockExercises = listOf(
        Exercise(
           id = 1,
            name = "Wide-Grip Barbell Bench Press",
            images = listOf("Wide-Grip_Barbell_Bench_Press/0.jpg", "Wide-Grip_Barbell_Bench_Press/1.jpg"),
            instructions = listOf("gg","haha"),
            primaryMuscles = listOf(("Chest"))
        ),
        Exercise(
            id = 2,
            name = "Leg Extensions",
            images = listOf("Leg_Extensions/0.jpg", "Leg_Extensions/1.jpg"),
            instructions = listOf("gg","haha"),
            primaryMuscles = listOf(("Quadriceps"))
        ),
        Exercise(
            id = 3,
            name = "Triceps Pushdown - V-Bar Attachment",
            images = listOf("Triceps_Pushdown_V-Bar_Attachment/0.jpg", "Triceps_Pushdown_V-Bar_Attachment/1.jpg"),
            instructions = listOf("gg","haha"),
            primaryMuscles = listOf(("Triceps"))
        ),
        Exercise(
            id = 4,
            name = "Incline Inner Biceps Curl",
            images = listOf("Incline_Inner_Biceps_Curl/0.jpg", "Incline_Inner_Biceps_Curl/1.jpg"),
            instructions = listOf("gg","haha"),
            primaryMuscles = listOf(("Biceps"))
        ),
        Exercise(
            id = 5,
            name = "Butt-Ups",
            images = listOf("Butt-Ups/0.jpg", "Butt-Ups/1.jpg"),
            instructions = listOf("gg","haha"),
            primaryMuscles = listOf(("Abdominals"))
        )
    )

}