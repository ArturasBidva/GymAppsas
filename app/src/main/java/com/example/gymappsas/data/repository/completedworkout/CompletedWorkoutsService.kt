package com.example.gymappsas.data.repository.completedworkout

import android.util.Log
import com.example.gymappsas.data.db.entities.CompletedWorkoutEntity
import com.example.gymappsas.data.db.models.completedworkout.CompletedWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.repository.workout.WorkoutService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class CompletedWorkoutsService @Inject constructor(
    private val completedWorkoutsRepository: CompletedWorkoutsRepository,
    private val workoutService: WorkoutService
) {
    suspend fun insertCompletedWorkout(workoutLocal: Workout) {
        Log.d("testukas2", workoutLocal.toString())
        completedWorkoutsRepository.insertCompletedWorkout(workoutLocal.toCompletedWorkout().toEntity())
    }

    suspend fun getCompletedWorkouts() : Flow<List<CompletedWorkout>> {
        return completedWorkoutsRepository.getCompletedWorkouts().map { it -> it.map { it.toCompletedWorkout() } }
    }

    private fun CompletedWorkout.toEntity(): CompletedWorkoutEntity {
        return CompletedWorkoutEntity(
            workoutId = this.workout.id,
            completedDate = this.completedDate,
            workoutTitle = this.workout.title
        )

    }

    private suspend fun CompletedWorkoutEntity.toCompletedWorkout(): CompletedWorkout {
        return CompletedWorkout(
            id = this.id,
            workout = getSelectedWorkout(this.workoutId)!!,
            completedDate = this.completedDate

        )
    }

    private suspend fun Workout.toCompletedWorkout(): CompletedWorkout {
        return CompletedWorkout(
            workout = getSelectedWorkout(this.id) ?: throw IllegalArgumentException("Workout not found"),
            completedDate = LocalDate.now()
        )
    }

    private suspend fun getSelectedWorkout(workoutId: Long): Workout? {
        return workoutService.getAllWorkouts().firstOrNull { it.firstOrNull()?.id == workoutId }?.firstOrNull()
    }
}
