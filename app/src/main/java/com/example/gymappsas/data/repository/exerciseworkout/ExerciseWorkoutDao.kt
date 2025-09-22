package com.example.gymappsas.data.repository.exerciseworkout

import androidx.room.Dao
import androidx.room.Update
import androidx.room.Upsert
import com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity

@Dao
interface ExerciseWorkoutDao {
    @Upsert
    suspend fun insertExerciseWorkout(exerciseWorkoutEntity: ExerciseWorkoutEntity) : Long

    @Update
    suspend fun updateExerciseWorkout(exerciseWorkoutEntity: ExerciseWorkoutEntity)

}