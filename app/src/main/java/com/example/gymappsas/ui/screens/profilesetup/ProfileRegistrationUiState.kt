package com.example.gymappsas.ui.screens.profilesetup

import androidx.compose.ui.graphics.Color
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.profile.Profile

sealed class ProfileSetupStep {
    data object NAME : ProfileSetupStep()
    data object METRICS : ProfileSetupStep()
    data object FITNESS_GOAL : ProfileSetupStep()
    data object FITNESS_LEVEL : ProfileSetupStep()
    data object WEEKLY_SCHEDULE : ProfileSetupStep()
    data object COMPLETED: ProfileSetupStep()

}

data class ProfileRegistrationUiState (
    val hasUserAttemptedSubmission: Boolean = false,
    val profileSetupStep : ProfileSetupStep = ProfileSetupStep.NAME,
    val metricsUiState: MetricsUiState = MetricsUiState(),
    val gender : Gender? = null,
    val profile : Profile? = null,
    val currentStep: Int = 1,
    val totalSteps: Int = 5,
    val selectedFitnessGoal: FitnessGoal? = null,
    val selectedFitnessLevel: FitnessLevel? = null,
    val selectedDays: List<WeekDay> = emptyList()
)

data class MetricsUiState(
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val ageError: String? = null,
    val weightError: String? = null,
    val heightError: String? = null,
    val dialogVisibility: Boolean = false,
    val name: String = "",
    val nameError: String? = null,
    val bmi : String? = "",
) {
    val hasError get() = listOf(ageError, weightError, heightError,nameError).any { it != null }
    val hasNameError get() = nameError != null
    val hasAgeError get() = ageError != null
    val hasWeightError get() = weightError != null
    val hasHeightError get() = heightError != null
    val isNameAndAgeValid get() = name.isNotBlank() && age.isNotBlank() && !hasNameError && !hasAgeError
}

enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
}
fun getBMICategory(bmi: Float): String {
    return when {
        bmi < 18.5f -> "Underweight"
        bmi < 25f -> "Normal weight"
        bmi < 30f -> "Overweight"
        else -> "Obese"
    }
}


enum class FitnessGoal(
    val displayName: String,
    val description: String,
    val icon: Int,
    val backgroundColor: Color,
    val primaryColor: Color,
    val selectedBackgroundColor: Color,
    val selectedBorderColor: Color
) {
    LOSE_WEIGHT(
        "Lose Weight",
        "Burn fat and get leaner",
        R.drawable.lose_weight_icon,
        Color(0xFFE8F5E8), // bg-green-100
        Color(0xFF10B981), // text-green-500
        Color(0xFFF0FDF4), // bg-green-50
        Color(0xFFBBF7D0)  // border-green-200
    ),
    BUILD_MUSCLE(
        "Build Muscle",
        "Increase strength and size",
        R.drawable.build_muscle_icon,
        Color(0xFFE3F2FD), // bg-blue-100
        Color(0xFF3B82F6), // text-blue-500
        Color(0xFFEFF6FF), // bg-blue-50
        Color(0xFFBFDBFE)  // border-blue-200
    ),
    IMPROVE_FITNESS(
        "Improve Fitness",
        "Enhance stamina and endurance",
        R.drawable.improve_fitness_icon,
        Color(0xFFFFF3E0), // bg-orange-100
        Color(0xFFF97316), // text-orange-500
        Color(0xFFFFFBEB), // bg-orange-50
        Color(0xFFFED7AA)  // border-orange-200
    ),
    MAINTAIN_HEALTH(
        "Maintain Health",
        "Stay active and healthy",
        R.drawable.ic_heart,
        Color(0xFFF3E8FF), // bg-purple-100
        Color(0xFF8B5CF6), // text-purple-500
        Color(0xFFFAF7FF), // bg-purple-50
        Color(0xFFE9D5FF)  // border-purple-200
    )


}

enum class FitnessLevel(
    val displayName: String,
    val description: String,
    val icon: Int,
    val primaryColor: Color,
    val backgroundColor: Color,
    val selectedBackgroundColor: Color,
    val selectedBorderColor: Color
) {
    BEGINNER(
        "Beginner",
        "New to fitness or returning after a break",
        R.drawable.beginner_icon,
        Color(0xFF22C55E), // green
        Color(0xFFDCFCE7), // bg-green-100
        Color(0xFFF0FDF4), // bg-green-50
        Color(0xFF22C55E)  // border-green-500
    ),
    INTERMEDIATE(
        "Intermediate",
        "Regular exercise with some experience",
        R.drawable.intermediate_icon,
        Color(0xFF3B82F6), // blue
        Color(0xFFDBEAFE), // bg-blue-100
        Color(0xFFEFF6FF), // bg-blue-50
        Color(0xFF3B82F6)  // border-blue-500
    ),
    ADVANCED(
        "Advanced",
        "Consistent training with good form",
        R.drawable.advanced_icon,
        Color(0xFFF97316), // orange
        Color(0xFFFED7AA), // bg-orange-200
        Color(0xFFFFFBEB), // bg-orange-50
        Color(0xFFF97316)  // border-orange-500
    ),
    EXPERT(
        "Expert",
        "Highly experienced with advanced techniques",
        R.drawable.expert_icon,
        Color(0xFFA855F7), // purple
        Color(0xFFE9D5FF), // bg-purple-200
        Color(0xFFFAF7FF), // bg-purple-50
        Color(0xFFA855F7)  // border-purple-500
    )

}

data class WeekDay(
    val id: Int,
    val name: String,
    val icon: Int,
    val backgroundColor: Color,
    val primaryColor: Color,
    val selectedBackgroundColor: Color,
    val selectedBorderColor: Color
)

val days = listOf(
    WeekDay(
        id = 0,
        name = "Monday",
        icon = R.drawable.ic_monday,
        backgroundColor = Color(0xFFF5F5F5),
        primaryColor = Color(0xFF4CAF50),
        selectedBackgroundColor = Color(0xFFE8F5E8),
        selectedBorderColor = Color(0xFF4CAF50)
    ),
    WeekDay(
        id = 1,
        name = "Tuesday",
        icon = R.drawable.ic_tuesday,
        backgroundColor = Color(0xFFF5F5F5),
        primaryColor = Color(0xFF2196F3),
        selectedBackgroundColor = Color(0xFFE3F2FD),
        selectedBorderColor = Color(0xFF2196F3)
    ),
    WeekDay(
        id = 2,
        name = "Wednesday",
        icon = R.drawable.ic_wensday,
        backgroundColor = Color(0xFFF5F5F5),
        primaryColor = Color(0xFFFF9800),
        selectedBackgroundColor = Color(0xFFFFF3E0),
        selectedBorderColor = Color(0xFFFF9800)
    ),
    WeekDay(
        id = 3,
        name = "Thursday",
        icon = R.drawable.ic_thursday,
        backgroundColor = Color(0xFFF5F5F5),
        primaryColor = Color(0xFFFFEB3B),
        selectedBackgroundColor = Color(0xFFFFFDE7),
        selectedBorderColor = Color(0xFFFFEB3B)
    ),
    WeekDay(
        id = 4,
        name = "Friday",
        icon = R.drawable.ic_friday,
        backgroundColor = Color(0xFFF5F5F5),
        primaryColor = Color(0xFF9C27B0),
        selectedBackgroundColor = Color(0xFFF3E5F5),
        selectedBorderColor = Color(0xFF9C27B0)
    ),
    WeekDay(
        id = 5,
        name = "Saturday",
        icon = R.drawable.ic_saturday,
        backgroundColor = Color(0xFFF5F5F5),
        primaryColor = Color(0xFF03DAC6),
        selectedBackgroundColor = Color(0xFFE0F7FA),
        selectedBorderColor = Color(0xFF03DAC6)
    ),
    WeekDay(
        id = 6,
        name = "Sunday",
        icon = R.drawable.ic_sunday,
        backgroundColor = Color(0xFFF5F5F5),
        primaryColor = Color(0xFFE91E63),
        selectedBackgroundColor = Color(0xFFFCE4EC),
        selectedBorderColor = Color(0xFFE91E63)
    )
)