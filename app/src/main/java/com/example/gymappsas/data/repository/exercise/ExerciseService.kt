package com.example.gymappsas.data.repository.exercise

import javax.inject.Inject


class ExerciseService @Inject constructor() {


    data class ExerciseJson(
        val name: String,
        val id: String,
        val images: List<String>,
        val instructions: List<String>,
        val primaryMuscles: List<String>,
        val equipment: String,
        val level: String
    )

}