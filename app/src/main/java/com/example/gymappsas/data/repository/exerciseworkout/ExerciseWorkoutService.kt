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

    suspend fun updateExerciseWorkoutData(exerciseWorkout: ExerciseWorkout) {
        exerciseWorkoutRepository.updateExerciseWorkoutData(exerciseWorkout = exerciseWorkout.toExerciseWorkoutEntity())
    }

    private fun ExerciseWorkout.toExerciseWorkoutEntity(): ExerciseWorkoutEntity {
        return ExerciseWorkoutEntity(
            completedCount = this.completedCount,
            maxWeight = this.maxWeight,
            goal = this.goal,
            exerciseId = this.exercise.id
        )
    }


}