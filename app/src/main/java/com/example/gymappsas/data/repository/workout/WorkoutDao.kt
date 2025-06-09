package com.example.gymappsas.data.repository.workout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity
import com.example.gymappsas.data.db.entities.WorkoutAndExerciseWorkoutCrossRef
import com.example.gymappsas.data.db.entities.WorkoutEntity
import com.example.gymappsas.data.db.entities.WorkoutWithExerciseWorkoutPair
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity) : Long

    @Upsert
    suspend fun insertWorkouts(workouts: List<WorkoutEntity>)

    @Transaction
    @Query("SELECT * FROM workouts")
    fun getWorkoutWithExerciseWorkoutByIdFlow(): Flow<List<WorkoutWithExerciseWorkoutPair>>

    @Transaction
    @Query("SELECT * FROM workouts")
    suspend fun getWorkoutWithExerciseWorkoutById(): List<WorkoutWithExerciseWorkoutPair>


    @Upsert
    suspend fun insertExerciseWorkouts(exerciseWorkoutEntities: List<ExerciseWorkoutEntity>)

    @Transaction
    @Query("SELECT * FROM  workouts")
    fun getAllWorkouts(): Flow<List<WorkoutWithExerciseWorkoutPair>>


    @Transaction
    @Query("DELETE FROM exerciseWorkout")
    suspend fun deleteExerciseWorkouts()

    @Transaction
    @Query("DELETE FROM workouts")
    suspend fun deleteWorkouts()

    @Transaction
    @Query("DELETE FROM workouts where id = :workoutId")
    suspend fun deleteWorkoutById(workoutId: Long)


    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Upsert
    suspend fun insertWorkoutAndExerciseWorkoutCrossRef(crossRefs: List<WorkoutAndExerciseWorkoutCrossRef>)

    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: Long): WorkoutEntity


}