package com.example.gymappsas.util

import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.ui.screens.profilesetup.FitnessGoal
import com.example.gymappsas.ui.screens.profilesetup.FitnessLevel

sealed class ValidationResult {
    data class Valid(val type: Validator.ValidationType) : ValidationResult()
    data class Invalid(val message: String, val type: Validator.ValidationType) : ValidationResult()
}


object Validator {

    fun validateProfile(profile: Profile): Boolean {
        return validateName(profile.name) is ValidationResult.Valid &&
                validateAge(profile.age.toString()) is ValidationResult.Valid &&
                validateWeight(profile.weight.toString()) is ValidationResult.Valid &&
                validateHeight(profile.height.toString()) is ValidationResult.Valid &&
                validateWorkoutDays(profile.workoutDays) is ValidationResult.Valid &&
                validateFitnessGoal(profile.fitnessGoal) is ValidationResult.Valid &&
                validateFitnessLevel(profile.fitnessLevel) is ValidationResult.Valid
    }

    private fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Invalid(
                "Name cannot be empty",
                type = ValidationType.NAME
            )

            name.length < 4 -> ValidationResult.Invalid(
                "Name too short",
                type = ValidationType.NAME
            )

            name.length > 25 -> ValidationResult.Invalid(
                "Name too long",
                type = ValidationType.NAME
            )
            name.any { it.isDigit() } -> ValidationResult.Invalid(
                "Name cannot contain numbers",
                type = ValidationType.NAME
            )

            else -> ValidationResult.Valid(ValidationType.NAME)
        }
    }

    private fun validateWorkoutDays(workoutDays: List<String>): ValidationResult {
        return when {
            workoutDays.isEmpty() -> ValidationResult.Invalid(
                "At least one workout day must be selected",
                ValidationType.WORKOUT_DAYS
            )
            workoutDays.size > 7 -> ValidationResult.Invalid(
                "Cannot select more than 7 days",
                ValidationType.WORKOUT_DAYS
            )
            else -> ValidationResult.Valid(ValidationType.WORKOUT_DAYS)
        }
    }

    private fun validateFitnessGoal(goal: FitnessGoal?): ValidationResult {
        return when (goal) {
            null -> ValidationResult.Invalid(
                "Fitness goal must be selected",
                ValidationType.WORKOUT_GOAL
            )
            else -> ValidationResult.Valid(ValidationType.WORKOUT_GOAL)
        }
    }

    private fun validateFitnessLevel(level: FitnessLevel?): ValidationResult {
        return when (level) {
            null -> ValidationResult.Invalid(
                "Fitness level must be selected", 
                ValidationType.FITNESS_LEVEL
            )
            else -> ValidationResult.Valid(ValidationType.FITNESS_LEVEL)
        }
    }

    fun validateMetric(type: ValidationType, value: String): ValidationResult {
        return when (type) {
            ValidationType.AGE -> validateAge(value)
            ValidationType.WEIGHT -> validateWeight(value)
            ValidationType.HEIGHT -> validateHeight(value)
            ValidationType.NAME -> validateName(value)
            else -> ValidationResult.Invalid("Use appropriate validateMetric overload", type)
        }
    }


    private fun validateAge(age: String): ValidationResult {
        return when (age.toIntOrNull()) {
            null -> ValidationResult.Invalid("Invalid age", ValidationType.AGE)
            !in 13..150 -> ValidationResult.Invalid("Age must be 13-150", ValidationType.AGE)
            else -> ValidationResult.Valid(ValidationType.AGE)
        }
    }

    private fun validateWeight(weight: String): ValidationResult {
        val weightValue = weight.toFloatOrNull()
        return when (weightValue) {
            null -> ValidationResult.Invalid("Invalid weight", ValidationType.WEIGHT)
            in 30f..160f -> ValidationResult.Valid(ValidationType.WEIGHT)
            else -> ValidationResult.Invalid(
                "Weight must be 30-160 kg",
                type = ValidationType.WEIGHT
            )
        }
    }

    private fun validateHeight(height: String): ValidationResult {
        val heightValue = height.toFloatOrNull()
        return when (heightValue) {
            null -> ValidationResult.Invalid("Invalid height", ValidationType.HEIGHT)
            in 100f..220f -> ValidationResult.Valid(ValidationType.HEIGHT)
            else -> ValidationResult.Invalid(
                "Height must be 100-220 cm",
                type = ValidationType.HEIGHT
            )
        }
    }

    enum class ValidationType {
        NAME, AGE, WEIGHT, HEIGHT, WORKOUT_DAYS, WORKOUT_GOAL, FITNESS_LEVEL,BMI
    }

}