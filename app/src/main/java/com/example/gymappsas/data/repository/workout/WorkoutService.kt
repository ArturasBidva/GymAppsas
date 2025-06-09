package com.example.gymappsas.data.repository.workout

import com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity
import com.example.gymappsas.data.db.entities.WorkoutAndExerciseWorkoutCrossRef
import com.example.gymappsas.data.db.entities.WorkoutEntity
import com.example.gymappsas.data.db.entities.WorkoutWithExerciseWorkoutPair
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import com.example.gymappsas.data.repository.exerciseworkout.ExerciseWorkoutService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutService @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    val exerciseWorkoutService: ExerciseWorkoutService,
    private val exerciseRepository: ExerciseRepository
) {

    /**
     * Create a workout, insert associated ExerciseWorkouts, and manage cross-references.
     */
    suspend fun createWorkoutWithExercises(workout: Workout): Long {
        return try {
            val workoutId = workoutRepository.insertWorkout(workout.toWorkoutEntity())
            workout.exerciseWorkouts.map { exerciseWorkout ->
                val exerciseWorkoutId = exerciseWorkoutService.insertExerciseWorkout(exerciseWorkout)
                insertWorkoutExerciseCrossRef(workoutId, exerciseWorkoutId)
                exerciseWorkoutId
            }

            workoutId
        } catch (e: Exception) {
            throw Exception("Failed to create workout with exercises: ${e.message}", e)
        }
    }

    /**
     * Insert a cross-reference between a workout and an exercise workout.
     */
    private suspend fun insertWorkoutExerciseCrossRef(workoutId: Long, exerciseWorkoutId: Long) {
        val crossRef = WorkoutAndExerciseWorkoutCrossRef(
            id = workoutId,
            exerciseWorkoutId = exerciseWorkoutId
        )
        workoutRepository.insertWorkoutAndExerciseWorkoutCrossRefs(listOf(crossRef))
    }

    /**
     * Fetch all workouts as a Flow.
     */
    fun getAllWorkouts(): Flow<List<Workout>> {
        return workoutRepository.getWorkoutsFlow().map { list ->
            list.map { it.toWorkout() }
        }
    }

    /**
     * Fetch all workouts synchronously.
     */
    suspend fun getAllWorkoutsSus(): List<Workout> {
        return workoutRepository.getWorkouts().map { it.toWorkout() }
    }

    /**
     * Delete a workout by its ID.
     */
    suspend fun deleteWorkoutById(workoutId: Long) {
        workoutRepository.deleteWorkoutById(workoutId)
    }

    /**
     * Fetch a single workout by its ID.
     */
    suspend fun getWorkoutById(workoutId: Long): Workout {
        return workoutRepository.getWorkoutByID(workoutId).toWorkout()
    }

    // Mappers

    private fun WorkoutWithExerciseWorkoutPair.toWorkout(): Workout {
        return Workout(
            id = this.workoutEntity.id,
            title = this.workoutEntity.title,
            description = this.workoutEntity.description,
            exerciseWorkouts = this.exerciseWorkouts.map { it.toExerciseWorkout() }
        )
    }

    private fun ExerciseWorkoutEntity.toExerciseWorkout(): ExerciseWorkout {
        return ExerciseWorkout(
            id = this.exerciseWorkoutId,
            completedCount = this.completedCount,
            weight = this.weight,
            goal = this.goal,
            exercise = exerciseRepository.exercises.value.first { this.exerciseId == it.id }

        )
    }

    private fun Workout.toWorkoutEntity(): WorkoutEntity {
        return WorkoutEntity(
            id = this.id,
            title = this.title,
            description = this.description
        )
    }

    private fun WorkoutEntity.toWorkout(): Workout {
        return Workout(
            id = this.id,
            title = this.title,
            description = this.description,
            exerciseWorkouts = emptyList()
        )
    }
}



