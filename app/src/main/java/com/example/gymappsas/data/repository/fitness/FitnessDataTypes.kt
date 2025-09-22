package com.example.gymappsas.data.repository.fitness

import kotlinx.serialization.Serializable

@Serializable
data class FitnessData(
    val steps: Int = 0,
    val calories: Float = 0f,
    val distance: Float = 0f,
    val activeMinutes: Int = 0,
    val heartRate: Int = 0,
    val date: String = "",
)

@Serializable
data class WorkoutSession(
    val name: String,
    val startTime: Long,
    val endTime: Long,
    val activityType: String,
    val calories: Float = 0f,
    val duration: Long // in milliseconds
)

@Serializable
data class FitnessGoals(
    val dailySteps: Int = 10000,
    val dailyCalories: Float = 2000f,
    val weeklyWorkouts: Int = 3,
    val activeMinutesPerDay: Int = 30
)

enum class ActivityType(val value: String) {
    RUNNING("running"),
    WALKING("walking"),
    CYCLING("cycling"),
    STRENGTH_TRAINING("strength_training"),
    YOGA("yoga"),
    SWIMMING("swimming"),
    OTHER("other")
}