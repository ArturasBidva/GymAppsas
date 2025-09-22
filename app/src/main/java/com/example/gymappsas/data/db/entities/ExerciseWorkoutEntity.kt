package com.example.gymappsas.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "exerciseWorkout")
data class ExerciseWorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val exerciseWorkoutId: Long = 0,
    val completedCount: Int = 0,
    val maxWeight: Float = 0F,
    val goal: Int = 0,
    val exerciseId: Long
) {
    override fun toString(): String {
        return "com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity(exerciseWorkoutId=$exerciseWorkoutId, " +
                "completedCount=$completedCount, weight=$maxWeight, goal=$goal, exerciseId=$exerciseId)"
    }
}