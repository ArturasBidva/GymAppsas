package com.example.gymappsas.data.repository.workoutvariants

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gymappsas.data.db.entities.WorkoutVariantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutVariantDao {
    @Insert
    suspend fun insert(variant: WorkoutVariantEntity): Long

    @Query("SELECT * FROM workout_variants WHERE workoutId = :workoutId ORDER BY createdAt DESC")
    fun getVariantsByWorkoutId(workoutId: Long): Flow<List<WorkoutVariantEntity>>

    @Query("SELECT * FROM workout_variants WHERE id = :variantId")
    suspend fun getById(variantId: Long): WorkoutVariantEntity?

    @Delete
    suspend fun delete(variant: WorkoutVariantEntity)

    @Update
    suspend fun update(variant: WorkoutVariantEntity)

    @Query("SELECT * FROM workout_variants ORDER BY createdAt DESC")
    fun getAllVariants(): Flow<List<WorkoutVariantEntity>>

    @Query("SELECT * FROM workout_variants WHERE lastUsedAt IS NOT NULL ORDER BY lastUsedAt DESC LIMIT :limit")
    fun getRecentlyUsedVariants(limit: Int = 5): Flow<List<WorkoutVariantEntity>>

    @Query("UPDATE workout_variants SET lastUsedAt = :timestamp WHERE id = :variantId")
    suspend fun updateLastUsedAt(variantId: Long, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM workout_variants WHERE workoutId = :workoutId ORDER BY lastUsedAt DESC LIMIT 1")
    suspend fun getLastUsedVariantForWorkout(workoutId: Long): WorkoutVariantEntity?
}