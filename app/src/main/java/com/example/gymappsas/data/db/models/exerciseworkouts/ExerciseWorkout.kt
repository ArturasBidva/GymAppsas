package com.example.gymappsas.data.db.models.exerciseworkouts

import android.os.Parcelable
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.util.MockExerciseData
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseWorkout(
    val id: Long = 0,
    var completedCount: Int = 0,
    val weight: Int = 0,
    val goal: Int = 4,
    val duration: Long = 60L * 1000,
    val breakTime: Long = 60L * 1000,
    val exercise: Exercise = MockExerciseData.mockExercises[0]
):Parcelable
