package com.example.gymappsas.data.repository.exerciseworkout

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity

@Dao
interface ExerciseWorkoutDao {
    @Upsert
    suspend fun insertExerciseWorkout(exerciseWorkoutEntity: ExerciseWorkoutEntity) : Long

    @Query("UPDATE exerciseWorkout SET weight = :weight WHERE exerciseWorkoutId = :exerciseWorkoutId")
    suspend fun setExerciseWorkoutWeightById(exerciseWorkoutId: Long, weight: Int)

}