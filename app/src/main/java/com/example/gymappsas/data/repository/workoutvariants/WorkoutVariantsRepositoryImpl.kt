package com.example.gymappsas.data.repository.workoutvariants

import com.example.gymappsas.data.db.entities.WorkoutVariantEntity
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.WorkoutVariant
import com.example.gymappsas.ui.screens.workoutprep.TrainingMethod
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutVariantsRepositoryImpl @Inject constructor(
    private val workoutVariantDao: WorkoutVariantDao,
    private val gson: Gson
) : WorkoutVariantsRepository {

    override suspend fun saveWorkoutVariant(workoutVariant: WorkoutVariant): Long {
        val entity = workoutVariant.toEntity(gson)
        return workoutVariantDao.insert(entity)
    }

    override suspend fun getWorkoutVariantsByWorkoutId(workoutId: Long): Flow<List<WorkoutVariant>> {
        return workoutVariantDao.getVariantsByWorkoutId(workoutId).map { entities ->
            entities.map { it.toModel(gson) }
        }
    }

    override suspend fun getWorkoutVariantById(variantId: Long): WorkoutVariant? {
        return workoutVariantDao.getById(variantId)?.toModel(gson)
    }

    override suspend fun deleteWorkoutVariant(variantId: Long) {
        workoutVariantDao.getById(variantId)?.let { workoutVariantDao.delete(it) }
    }

    override suspend fun updateWorkoutVariant(workoutVariant: WorkoutVariant) {
        val entity = workoutVariant.toEntity(gson)
        workoutVariantDao.update(entity)
    }
}

private fun WorkoutVariant.toEntity(gson: Gson): WorkoutVariantEntity {
    return WorkoutVariantEntity(
        id = 0L,
        workoutId = id,
        name = name,
        trainingMethod = trainingMethod,
        restTimeSeconds = restTimeSeconds,
        exerciseWorkoutsJson = gson.toJson(exercises),
        createdAt = System.currentTimeMillis(),
        lastUsedAt = lastUsedAt,
        notes = description
    )
}

private fun WorkoutVariantEntity.toModel(gson: Gson): WorkoutVariant {
    val type = object : com.google.gson.reflect.TypeToken<List<ExerciseWorkout>>() {}.type
    val exercises: List<ExerciseWorkout> = gson.fromJson(this.exerciseWorkoutsJson, type)
    return WorkoutVariant(
        id = workoutId,
        name = name,
        trainingMethod = trainingMethod,
        restTimeSeconds = restTimeSeconds,
        lastUsedAt = lastUsedAt,
        createdAt = createdAt.toString(),
        exercises = exercises,
        description = notes
    )
}