package com.example.gymappsas.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exerciseWorkout")
data class ExerciseWorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val exerciseWorkoutId: Long = 0,
    val completedCount: Int = 0,
    val weight: Int = 0,
    val goal: Int = 0,
    val exerciseId: Long
) {
    override fun toString(): String {
        return "com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity(exerciseWorkoutId=$exerciseWorkoutId, " +
                "completedCount=$completedCount, weight=$weight, goal=$goal, exerciseId=$exerciseId)"
    }
}