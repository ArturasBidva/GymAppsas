package com.example.gymappsas.data.repository.workout
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.example.gymappsas.data.db.entities.WorkoutAndExerciseWorkoutCrossRef
import com.example.gymappsas.data.db.entities.WorkoutEntity
import com.example.gymappsas.data.db.entities.WorkoutWithExerciseWorkoutPair
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao
) {

    suspend fun deleteWorkoutById(workoutId: Long) {
        workoutDao.deleteWorkoutById(workoutId = workoutId)
    }
    suspend fun updateWorkout(workout: WorkoutEntity) {
        workoutDao.updateWorkout(workout)
    }


    fun getWorkoutsFlow(): Flow<List<WorkoutWithExerciseWorkoutPair>> {
        return workoutDao.getWorkoutWithExerciseWorkoutByIdFlow()
    }

    suspend fun getWorkouts(): List<WorkoutWithExerciseWorkoutPair> {
        return workoutDao.getWorkoutWithExerciseWorkoutById()
    }

    suspend fun insertWorkout(workout: WorkoutEntity) : Long {
        return workoutDao.insertWorkout(workout)
    }

    suspend fun getWorkoutByID(workoutId: Long): WorkoutEntity {
        return workoutDao.getWorkoutById(workoutId)
    }


    suspend fun insertWorkoutAndExerciseWorkoutCrossRefs(
        workoutAndExerciseWorkoutCrossRef: List<WorkoutAndExerciseWorkoutCrossRef>
    ) {
        try {
            // Insert into the database
            workoutDao.insertWorkoutAndExerciseWorkoutCrossRef(workoutAndExerciseWorkoutCrossRef)
        } catch (e: SQLiteConstraintException) {
            Log.e("DatabaseError", "Foreign key constraint failed: ${e.message}")
            // Handle the foreign key constraint violation
        } catch (e: Exception) {
            Log.e("DatabaseError", "An error occurred: ${e.message}")
            // Handle other exceptions
        }
    }

    suspend fun deleteWorkouts() {
        workoutDao.deleteWorkouts()
    }
}