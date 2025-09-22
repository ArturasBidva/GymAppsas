package com.example.gymappsas.ui.screens.profilesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.data.repository.profile.ProfileService
import com.example.gymappsas.util.ValidationResult
import com.example.gymappsas.util.Validator
import com.example.gymappsas.util.Validator.validateMetric
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(private val profileService: ProfileService) :
    ViewModel() {
    init {
        checkUserExistence()
    }

    sealed class ProfileSetupState {
        data object Loading : ProfileSetupState()
        data class Loaded(val userExists: Boolean) : ProfileSetupState()

    }

    private val _state = MutableStateFlow<ProfileSetupState>(ProfileSetupState.Loading)
    val state: StateFlow<ProfileSetupState> = _state

    private val _uiState = MutableStateFlow(ProfileRegistrationUiState())
    val uiState: StateFlow<ProfileRegistrationUiState> = _uiState

    private fun navigateToGenderStep() {
        _uiState.update { it.copy(profileSetupStep = ProfileSetupStep.METRICS, currentStep = 2) }
    }

    private fun checkUserExistence() {
        viewModelScope.launch {
            profileService.getProfile().collect { user ->
                if (user != null) {
                    _state.value = ProfileSetupState.Loaded(true)
                }

            }
        }
    }

    fun onGenderChanged(gender: Gender) {
        _uiState.update {
            it.copy(gender = gender)
        }
    }


    fun onMetricChanged(type: Validator.ValidationType, value: String) {
        // First update the specific field that changed
        _uiState.update { state ->
            val updatedMetrics = when (type) {
                Validator.ValidationType.AGE -> state.metricsUiState.copy(age = value)
                Validator.ValidationType.WEIGHT -> state.metricsUiState.copy(weight = value)
                Validator.ValidationType.HEIGHT -> state.metricsUiState.copy(height = value)
                Validator.ValidationType.NAME -> state.metricsUiState.copy(name = value)
                else -> state.metricsUiState
            }

            val w = updatedMetrics.weight.toFloatOrNull()
            val h = updatedMetrics.height.toFloatOrNull()
            val bmi = if (w != null && h != null && w > 0 && h > 0) {
                w / ((h / 100f) * (h / 100f))
            } else null

            state.copy(metricsUiState = updatedMetrics.copy(bmi = bmi?.toString()))
        }

        // Validate only the field that changed
        validateSpecificMetric(type, value)
    }

    private fun validateSpecificMetric(type: Validator.ValidationType, value: String) {
        val result = validateMetric(type, value)

        _uiState.update { currentState ->
            currentState.copy(
                metricsUiState = when (type) {
                    Validator.ValidationType.AGE -> currentState.metricsUiState.copy(
                        ageError = if (result is ValidationResult.Invalid) result.message else null
                    )

                    Validator.ValidationType.WEIGHT -> currentState.metricsUiState.copy(
                        weightError = if (result is ValidationResult.Invalid) result.message else null
                    )

                    Validator.ValidationType.HEIGHT -> currentState.metricsUiState.copy(
                        heightError = if (result is ValidationResult.Invalid) result.message else null
                    )

                    Validator.ValidationType.NAME -> currentState.metricsUiState.copy(
                        nameError = if (result is ValidationResult.Invalid) result.message else null
                    )

                    Validator.ValidationType.WORKOUT_DAYS -> TODO()
                    Validator.ValidationType.WORKOUT_GOAL -> TODO()
                    Validator.ValidationType.FITNESS_LEVEL -> TODO()
                    Validator.ValidationType.BMI -> TODO()
                }
            )
        }
    }

    private fun validateAllMetrics() {
        val ageResult = validateMetric(
            Validator.ValidationType.AGE,
            uiState.value.metricsUiState.age
        )
        val weightResult = validateMetric(
            Validator.ValidationType.WEIGHT,
            uiState.value.metricsUiState.weight
        )
        val heightResult = validateMetric(
            Validator.ValidationType.HEIGHT,
            uiState.value.metricsUiState.height
        )
        val nameResult = validateMetric(
            Validator.ValidationType.NAME,
            uiState.value.metricsUiState.name
        )

        _uiState.update { currentState ->
            currentState.copy(
                metricsUiState = currentState.metricsUiState.copy(
                    ageError = if (ageResult is ValidationResult.Invalid) ageResult.message else null,
                    weightError = if (weightResult is ValidationResult.Invalid) weightResult.message else null,
                    heightError = if (heightResult is ValidationResult.Invalid) heightResult.message else null,
                    nameError = if (nameResult is ValidationResult.Invalid) nameResult.message else null
                )
            )
        }

        if (
            ageResult is ValidationResult.Valid &&
            weightResult is ValidationResult.Valid &&
            heightResult is ValidationResult.Valid &&
            nameResult is ValidationResult.Valid
        ) {

            _uiState.update {
                it.copy(
                    profile = Profile(
                        name = it.metricsUiState.name,
                        age = it.metricsUiState.age.toInt(),
                        weight = it.metricsUiState.weight.toFloat(),
                        height = it.metricsUiState.height.toFloat(),
                        gender = it.gender,
                        bmi = it.metricsUiState.bmi?.toFloat() ?: 0f
                    )
                )
            }
        }
    }

    fun proceedToGenderStep() {
        _uiState.update { it.copy(hasUserAttemptedSubmission = true) }
        if (uiState.value.metricsUiState.nameError == null && uiState.value.metricsUiState.ageError == null) {
            navigateToGenderStep()
        }
    }

    fun proceedToGoalStep() {
        _uiState.update { it.copy(hasUserAttemptedSubmission = true) }
        validateAllMetrics()
        if (uiState.value.metricsUiState.weightError == null && uiState.value.metricsUiState.heightError == null) {
            navigateToGoalStep()
        }
    }

    fun selectedGoal(fitnessGoal: FitnessGoal) {
        _uiState.update { it.copy(selectedFitnessGoal = fitnessGoal) }
    }

    fun navigateToGoalStep() {
        _uiState.update {
            it.copy(
                profileSetupStep = ProfileSetupStep.FITNESS_GOAL,
                currentStep = 3
            )
        }
    }

    fun proceedToLevelStep() {
        _uiState.update {
            it.copy(
                profileSetupStep = ProfileSetupStep.FITNESS_LEVEL,
                currentStep = 4
            )
        }
        if (uiState.value.selectedFitnessGoal != null) {
            navigateToLevelStep()
        }
    }

    private fun navigateToLevelStep() {
        _uiState.update {
            it.copy(
                profileSetupStep = ProfileSetupStep.FITNESS_LEVEL,
                currentStep = 4
            )
        }

    }

    fun onFitnessLevelSelection(fitnessLevel: FitnessLevel) {
        _uiState.update { it.copy(selectedFitnessLevel = fitnessLevel) }
    }


    fun proceedToDaySelectionStep() {
        if (uiState.value.selectedFitnessLevel != null) {
            _uiState.update {
                it.copy(
                    profileSetupStep = ProfileSetupStep.WEEKLY_SCHEDULE,
                    currentStep = 5
                )
            }
        }
    }

    fun onWeekDaySelected(day: WeekDay) {
        val current = _uiState.value.selectedDays.toMutableList()
        if (current.contains(day)) {
            current.remove(day)
        } else {
            current.add(day)
        }
        _uiState.value = _uiState.value.copy(selectedDays = current)
    }

    fun backToPreviousStep() {
        _uiState.update { current ->
            val previousStep = when (current.profileSetupStep) {
                ProfileSetupStep.METRICS -> ProfileSetupStep.NAME
                ProfileSetupStep.FITNESS_GOAL -> ProfileSetupStep.METRICS
                ProfileSetupStep.FITNESS_LEVEL -> ProfileSetupStep.FITNESS_GOAL
                ProfileSetupStep.WEEKLY_SCHEDULE -> ProfileSetupStep.FITNESS_LEVEL
                else -> current.profileSetupStep
            }

            val previousStepIndex = when (previousStep) {
                ProfileSetupStep.NAME -> 1
                ProfileSetupStep.METRICS -> 2
                ProfileSetupStep.FITNESS_GOAL -> 3
                ProfileSetupStep.FITNESS_LEVEL -> 4
                ProfileSetupStep.WEEKLY_SCHEDULE -> 5
                ProfileSetupStep.COMPLETED -> 6
            }

            current.copy(
                profileSetupStep = previousStep,
                currentStep = previousStepIndex
            )
        }
    }

    fun createProfile() {
        val profile = Profile(
            name = _uiState.value.metricsUiState.name,
            age = _uiState.value.metricsUiState.age.toInt(),
            weight = _uiState.value.metricsUiState.weight.toFloat(),
            height = _uiState.value.metricsUiState.height.toFloat(),
            fitnessGoal = _uiState.value.selectedFitnessGoal,
            fitnessLevel = _uiState.value.selectedFitnessLevel,
            workoutDays = _uiState.value.selectedDays.map { it.name},
            gender = _uiState.value.gender,
            bmi = _uiState.value.metricsUiState.bmi?.toFloatOrNull() ?: 0f
        )
        viewModelScope.launch {
            profileService.saveProfile(profile = profile)
        }
    }
}

