package com.example.gymappsas.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gymappsas.data.db.models.steps.StepsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSteps(stepsEntity: StepsEntity)

    @Query("SELECT * FROM daily_steps WHERE date = :date")
    suspend fun getStepsForDate(date: String): StepsEntity?

    @Query("SELECT * FROM daily_steps WHERE date = :date LIMIT 1")
    fun getStepsFlowForDate(date: String): Flow<StepsEntity?>

    @Query("SELECT * FROM daily_steps WHERE date = DATE('now') ORDER BY date DESC LIMIT 1")
    fun getTodayStepsFlow(): Flow<StepsEntity?>

    @Query("SELECT * FROM daily_steps WHERE date >= DATE('now', '-7 days') ORDER BY date DESC")
    fun getWeeklyStepsFlow(): Flow<List<StepsEntity>>
}