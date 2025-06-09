package com.example.gymappsas.data.repository.completedworkout
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.gymappsas.data.db.entities.CompletedWorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedWorkoutDao {
    @Upsert
    suspend fun insertCompletedWorkout(completedWorkoutEntity: CompletedWorkoutEntity)

    @Transaction
    @Query("SELECT * FROM completed_workouts")
    fun getCompletedWorkouts(): Flow<List<CompletedWorkoutEntity>>



}