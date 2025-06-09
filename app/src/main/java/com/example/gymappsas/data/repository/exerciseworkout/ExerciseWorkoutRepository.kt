package com.example.gymappsas.data.repository.exerciseworkout

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity
import javax.inject.Inject

class ExerciseWorkoutRepository @Inject constructor(
    private val exerciseWorkoutDao: ExerciseWorkoutDao
) {
    suspend fun insertExerciseWorkout(exerciseWorkoutEntity: ExerciseWorkoutEntity): Long {
        return try {
            // Insert into the database and return the generated ID
            exerciseWorkoutDao.insertExerciseWorkout(exerciseWorkoutEntity)
        } catch (e: SQLiteConstraintException) {
            Log.e("DatabaseError", "Foreign key constraint failed: ${e.message}")
            // Return a special value or throw the exception again if necessary
            -1L // Returning -1 to indicate failure due to foreign key constraint
        } catch (e: Exception) {
            Log.e("DatabaseError", "An error occurred: ${e.message}")
            // Return a special value or handle the exception as per your need
            -1L // Returning -1 to indicate a generic failure
        }
    }

    suspend fun updateExerciseWorkoutWeight(exerciseWorkoutId: Long, weight: Int) {
        exerciseWorkoutDao.setExerciseWorkoutWeightById(
            exerciseWorkoutId = exerciseWorkoutId,
            weight = weight
        )
    }

}