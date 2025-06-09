package com.example.gymappsas.util

object GetImagePath {
    fun getExerciseImagePath(category: String, exerciseName: String, imageIndex: Int = 0): String {
        // Base path for assets
        val basePath = "file:///android_asset/exercisesbycategories"

        // Convert category and exercise name to match folder naming conventions
        val normalizedCategory = category.lowercase()
        val normalizedExerciseName = exerciseName.lowercase().replace(" ", "_")

        // Construct the final path based on conventions
        return "$basePath/$normalizedCategory/$normalizedExerciseName/$imageIndex.jpg"
    }
}