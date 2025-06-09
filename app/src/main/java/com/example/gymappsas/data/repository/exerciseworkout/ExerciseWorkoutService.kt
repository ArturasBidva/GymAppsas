package com.example.gymappsas.data.repository.exerciseworkout

import com.example.gymappsas.data.db.entities.ExerciseWorkoutEntity
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import javax.inject.Inject

class ExerciseWorkoutService @Inject constructor(
    private val exerciseWorkoutRepository: ExerciseWorkoutRepository
) {
    suspend fun insertExerciseWorkout(exerciseWorkout: ExerciseWorkout): Long {
        return exerciseWorkoutRepository.insertExerciseWorkout(exerciseWorkoutEntity = exerciseWorkout.toExerciseWorkoutEntity())

    }

    suspend fun updateExerciseWorkoutWeight(exerciseWorkoutId: Long, weight: Int) {
        exerciseWorkoutRepository.updateExerciseWorkoutWeight(
            exerciseWorkoutId = exerciseWorkoutId,
            weight = weight
        )
    }

    private fun ExerciseWorkout.toExerciseWorkoutEntity(): ExerciseWorkoutEntity {
        return ExerciseWorkoutEntity(
            completedCount = this.completedCount,
            weight = this.weight,
            goal = this.goal,
            exerciseId = this.exercise.id
        )
    }


}