package com.example.gymappsas.data.repository.workoutvariants

import com.example.gymappsas.data.db.models.workouts.WorkoutVariant
import kotlinx.coroutines.flow.Flow

interface WorkoutVariantsRepository {
    suspend fun saveWorkoutVariant(workoutVariant: WorkoutVariant): Long
    suspend fun getWorkoutVariantsByWorkoutId(workoutId: Long): Flow<List<WorkoutVariant>>
    suspend fun getWorkoutVariantById(variantId: Long): WorkoutVariant?
    suspend fun deleteWorkoutVariant(variantId: Long)
    suspend fun updateWorkoutVariant(workoutVariant: WorkoutVariant)
}