package com.example.gymappsas.data.repository.completedworkout
import android.util.Log
import com.example.gymappsas.data.db.entities.CompletedWorkoutEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompletedWorkoutsRepository @Inject constructor(
   private val completedWorkoutDao: CompletedWorkoutDao
) {
    suspend fun insertCompletedWorkout(completedWorkout: CompletedWorkoutEntity){
        Log.d("testukas3", completedWorkout.toString())
        completedWorkoutDao.insertCompletedWorkout(completedWorkoutEntity = completedWorkout)
    }

    fun getCompletedWorkouts(): Flow<List<CompletedWorkoutEntity>> {
        return completedWorkoutDao.getCompletedWorkouts()
    }
}