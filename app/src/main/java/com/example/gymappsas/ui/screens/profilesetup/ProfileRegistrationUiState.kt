package com.example.gymappsas.ui.screens.profilesetup

import com.example.gymappsas.data.db.models.profile.Profile

sealed class ProfileSetupStep {
    data object NAME : ProfileSetupStep()
    data object GENDER : ProfileSetupStep()
    data object METRICS : ProfileSetupStep()
    data object COMPLETED: ProfileSetupStep()

}

data class ProfileRegistrationUiState (
    val name: String = "",
    val hasUserAttemptedSubmission: Boolean = false,
    val profileSetupStep : ProfileSetupStep = ProfileSetupStep.NAME,
    val nameError: String? = null,
    val metricsUiState: MetricsUiState = MetricsUiState(),
    val gender : Gender = Gender.MALE,
    val profile : Profile? = null,
) {
    val hasNameError get() = nameError != null
}

data class MetricsUiState(
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val ageError: String? = null,
    val weightError: String? = null,
    val heightError: String? = null,
    val dialogVisibility: Boolean = false
) {
    val hasError get() = listOf(ageError, weightError, heightError).any { it != null }
}

enum class Gender(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    NONE("None")
}