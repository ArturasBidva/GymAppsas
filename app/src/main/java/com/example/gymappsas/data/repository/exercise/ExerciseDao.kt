package com.example.gymappsas.data.repository.exercise

import androidx.room.Dao
import androidx.room.Upsert
import com.example.gymappsas.data.db.entities.ExerciseEntity

@Dao
interface ExerciseDao {
    @Upsert
    suspend fun insertExercise(exerciseEntity: ExerciseEntity)
}