package com.example.gymappsas.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["id", "exerciseWorkoutId"],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseWorkoutEntity::class,
            parentColumns = ["exerciseWorkoutId"],
            childColumns = ["exerciseWorkoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["exerciseWorkoutId"])] // Add index for exerciseWorkoutId
)
data class WorkoutAndExerciseWorkoutCrossRef(
    val id: Long,
    val exerciseWorkoutId: Long
)