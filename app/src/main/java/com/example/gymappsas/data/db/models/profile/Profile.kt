package com.example.gymappsas.data.db.models.profile

import android.os.Parcelable
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.ui.screens.profilesetup.FitnessGoal
import com.example.gymappsas.ui.screens.profilesetup.FitnessLevel
import com.example.gymappsas.ui.screens.profilesetup.Gender
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class Profile(
    val id: Long = 0,
    val name: String = "",
    val age: Int = 0,
    val joinDate: String = formatedDate(),
    val weeklyTrainingMinutes: Int = 0,
    val weeklyWorkoutCount : Int = 0,
    val weight: Float = 0f,
    val height: Float = 0f,
    val gender: Gender? = null,
    val streak : Int = 0,
    val fitnessGoal: FitnessGoal? = null,
    val workoutDays: List<String> = emptyList(),
    val fitnessLevel: FitnessLevel? = null,
    val workoutWeeklyProgress : Float = 0f,
    val workouts : List<Workout> = emptyList(),
    val bmi : Float = 0f
)
    : Parcelable
fun formatedDate(): String {
    val current = LocalDateTime.now()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatted = current.format(formatter)

    return formatted
}
data class WorkoutDayStatus(
    val day: String,
    val isCompleted: Boolean
)
