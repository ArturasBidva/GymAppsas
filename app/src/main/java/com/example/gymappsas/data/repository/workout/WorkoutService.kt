package com.example.gymappsas.data.repository.workout

import com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity
import com.example.gymappsas.data.db.entities.WorkoutAndExerciseWorkoutCrossRef
import com.example.gymappsas.data.db.entities.WorkoutEntity
import com.example.gymappsas.data.db.entities.WorkoutWithExerciseWorkoutPair
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.db.models.workouts.WorkoutCategory
import com.example.gymappsas.data.db.models.workouts.WorkoutVariant
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import com.example.gymappsas.data.repository.exerciseworkout.ExerciseWorkoutService
import com.example.gymappsas.data.repository.workoutvariants.WorkoutVariantDao
import com.example.gymappsas.util.Resource
import com.example.gymappsas.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutService @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    val exerciseWorkoutService: ExerciseWorkoutService,
    private val exerciseRepository: ExerciseRepository,
    private val workoutVariantDao: WorkoutVariantDao
) {

    /**
     * Create a workout, insert associated ExerciseWorkouts, and manage cross-references.
     */
    suspend fun createWorkoutWithExercises(workout: Workout): Long? {
        return try {
            val workoutId = workoutRepository.insertWorkout(workout.toWorkoutEntity())
            workout.exerciseWorkouts.forEach { exerciseWorkout ->
                val exerciseWorkoutId =
                    exerciseWorkoutService.insertExerciseWorkout(exerciseWorkout)
                insertWorkoutExerciseCrossRef(workoutId, exerciseWorkoutId)
            }
            workoutId
        } catch (e: Exception) {
            null
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

    suspend fun updateWorkout(workout: Workout) {
        workoutRepository.updateWorkout(workout.toWorkoutEntity())
    }

    /**
     * Fetch all workouts as a Flow, with variants included.
     */
    fun getAllWorkouts(): Flow<List<Workout>> {
        return combine(
            workoutRepository.getWorkoutsFlow().map { list -> list.map { it.toWorkout() } },
            workoutVariantDao.getAllVariants()
        ) { plainWorkouts, allVariants ->
            plainWorkouts.map { workout ->
                val variants = allVariants.filter { it.workoutId == workout.id }.map { entity ->
                    WorkoutVariant(
                        id = entity.id,
                        name = entity.name,
                        trainingMethod = entity.trainingMethod,
                        restTimeSeconds = entity.restTimeSeconds,
                        lastUsedAt = entity.lastUsedAt,
                        estimatedDuration = estimateWorkoutDuration(
                            entity.restTimeSeconds,
                            workout.exerciseWorkouts.size
                        ),
                        isFavourite = workout.isFavorite
                    )
                }
                workout.copy(variants = variants.ifEmpty { null })
            }
        }
    }

    /**
     * Mark a variant as used (called when starting a workout)
     */
    suspend fun markVariantAsUsed(variantId: Long) {
        workoutVariantDao.updateLastUsedAt(variantId)
    }

    /**
     * Get the last used variant for a specific workout
     */
    suspend fun getLastUsedVariantForWorkout(workoutId: Long): WorkoutVariant? {
        val entity = workoutVariantDao.getLastUsedVariantForWorkout(workoutId)
        return entity?.let {
            val workout = getWorkoutById(workoutId)
            WorkoutVariant(
                id = it.id,
                name = it.name,
                trainingMethod = it.trainingMethod,
                restTimeSeconds = it.restTimeSeconds,
                lastUsedAt = it.lastUsedAt,
                estimatedDuration = estimateWorkoutDuration(
                    it.restTimeSeconds,
                    workout.exerciseWorkouts.size
                )
            )
        }
    }

    private fun estimateWorkoutDuration(restTimeSeconds: Int, exerciseCount: Int): Long {
        // Estimate: 2 minutes per exercise + rest time between sets
        val exerciseTimeMinutes = exerciseCount * 2
        val restTimeMinutes =
            (exerciseCount * 3 * restTimeSeconds) / 60 // 3 sets per exercise average
        return exerciseTimeMinutes.toLong() + restTimeMinutes
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

    suspend fun addWorkoutToFavourites(workoutId: Long) : Resource<Unit> {
        return try {
            val workout = getWorkoutById(workoutId)
            workoutRepository.updateWorkout(
                workout.toWorkoutEntity().copy(isFavourite = true)
            )
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(UiText.DynamicString(e.localizedMessage ?: "Unknown error"))
        }
    }

    suspend fun removeWorkoutFromFavourites(workoutId: Long) : Resource<Unit> {
        return try {

        getWorkoutById(workoutId = workoutId).apply {
            workoutRepository.updateWorkout(this.toWorkoutEntity().copy(isFavourite = false))
        }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(UiText.DynamicString(e.localizedMessage ?: "Unknown error"))
        }
    }

    // Mappers

    private fun WorkoutWithExerciseWorkoutPair.toWorkout(): Workout {
        return Workout(
            id = this.workoutEntity.id,
            title = this.workoutEntity.title,
            description = this.workoutEntity.description,
            exerciseWorkouts = this.exerciseWorkouts.map { it.toExerciseWorkout() },
            isFavorite = this.workoutEntity.isFavourite
        )
    }

    private fun ExerciseWorkoutEntity.toExerciseWorkout(): ExerciseWorkout {
        return ExerciseWorkout(
            id = this.exerciseWorkoutId,
            completedCount = this.completedCount,
            maxWeight = this.maxWeight,
            goal = this.goal,
            exercise = exerciseRepository.exercises.value.first { this.exerciseId == it.id }

        )
    }

    private fun Workout.toWorkoutEntity(): WorkoutEntity {
        return WorkoutEntity(
            id = this.id,
            title = this.title,
            description = this.description,
            isFavourite = this.isFavorite,
            category = this.category?.name ?: ""
        )
    }

    private fun WorkoutEntity.toWorkout(): Workout {
        return Workout(
            id = this.id,
            title = this.title,
            description = this.description,
            exerciseWorkouts = emptyList(),
            isFavorite = this.isFavourite,
            category = WorkoutCategory.entries.first { it.name == this.category }
        )
    }
}
