package com.example.gymappsas.data.db.models.exercisecategory

sealed class ExerciseCategory(val name: String) {
    data object Biceps : ExerciseCategory("biceps")
    data object Lats : ExerciseCategory("lats")
    data object Abdominals : ExerciseCategory("abdominals")
    data object Glutes : ExerciseCategory("glutes")
    data object Quadriceps : ExerciseCategory("quadriceps")
    data object Shoulders : ExerciseCategory("shoulders")
    data object Triceps : ExerciseCategory("triceps")
    data object Chest : ExerciseCategory("chest")
    companion object {
        fun fromName(name: String): ExerciseCategory? {
            return when (name) {
                Biceps.name -> Biceps
                Lats.name -> Lats
                Abdominals.name -> Abdominals
                Glutes.name -> Glutes
                Quadriceps.name -> Quadriceps
                Shoulders.name -> Shoulders
                Triceps.name -> Triceps
                Chest.name -> Chest

                else -> null
            }
        }

        fun getAllCategories(): List<ExerciseCategory> {
            return listOf(Biceps,Lats,Abdominals,Glutes,Quadriceps,Shoulders,Triceps,Chest)
        }
    }
}