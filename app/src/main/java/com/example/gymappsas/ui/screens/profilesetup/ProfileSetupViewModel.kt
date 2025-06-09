package com.example.gymappsas.ui.screens.profilesetup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.data.repository.profile.ProfileService
import com.example.gymappsas.util.PickerType
import com.example.gymappsas.util.ValidationResult
import com.example.gymappsas.util.Validator
import com.example.gymappsas.util.Validator.validateMetric
import com.example.gymappsas.util.ageOptions
import com.example.gymappsas.util.heightOptions
import com.example.gymappsas.util.weightOptions
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

    private val _showPicker = mutableStateOf(PickerType.NONE)
    val showPicker: State<PickerType> = _showPicker

    private fun navigateToGenderStep() {
        _uiState.update { it.copy(profileSetupStep = ProfileSetupStep.GENDER) }
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


    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
        if (uiState.value.hasUserAttemptedSubmission) {
            validateNameStep()
        }
    }

    fun onGenderChanged(gender: Gender) {
        _uiState.update {
            it.copy(gender = gender)
        }
    }

    fun getPickerOptions(type: PickerType): List<String> {
        return when (type) {
            PickerType.AGE -> ageOptions
            PickerType.WEIGHT -> weightOptions
            PickerType.HEIGHT -> heightOptions
            else -> emptyList()
        }
    }

    private fun validateNameStep() {
        val result = validateMetric(Validator.ValidationType.NAME, uiState.value.name)
        _uiState.update {
            it.copy(
                hasUserAttemptedSubmission = true,
                nameError = when (result) {
                    is ValidationResult.Invalid -> result.message
                    else -> null
                }
            )
        }

    }

    fun onMetricChanged(type: Validator.ValidationType, value: String) {
        _uiState.update { currentState ->
            currentState.copy(
                metricsUiState = when (type) {
                    Validator.ValidationType.AGE -> currentState.metricsUiState.copy(age = value)
                    Validator.ValidationType.WEIGHT -> currentState.metricsUiState.copy(weight = value)
                    Validator.ValidationType.HEIGHT -> currentState.metricsUiState.copy(height = value)
                    else -> currentState.metricsUiState
                }
            )
        }
    }

    fun showPickerDialog(type: PickerType) {
        _showPicker.value = type
    }

    fun hidePickerDialog() {
        _showPicker.value = PickerType.NONE
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

        _uiState.update { currentState ->
            currentState.copy(
                metricsUiState = currentState.metricsUiState.copy(
                    ageError = if (ageResult is ValidationResult.Invalid) ageResult.message else null,
                    weightError = if (weightResult is ValidationResult.Invalid) weightResult.message else null,
                    heightError = if (heightResult is ValidationResult.Invalid) heightResult.message else null
                )
            )
        }

        if (
            ageResult is ValidationResult.Valid &&
            weightResult is ValidationResult.Valid &&
            heightResult is ValidationResult.Valid
        ) {
            _uiState.update {
                it.copy(
                    profile = Profile(
                        name = it.name,
                        age = it.metricsUiState.age.toInt(),
                        weight = it.metricsUiState.weight.toFloat(),
                        height = it.metricsUiState.height.toFloat(),
                        gender = it.gender
                    )
                )
            }
        }
    }

    fun proceedToGenderStep() {
        validateNameStep()
        if (uiState.value.nameError == null) {
            navigateToGenderStep()
        }
    }

    fun proceedToMetricsStep() {
        if (uiState.value.gender != Gender.NONE) {
            _uiState.update { it.copy(profileSetupStep = ProfileSetupStep.METRICS) }
        }
    }

    fun saveProfile() {
        validateAllMetrics()
        viewModelScope.launch {
            uiState.value.profile.let { it ->
                if (it != null) {
                    profileService.saveProfile(it)
                }
                _uiState.update { it.copy(profileSetupStep = ProfileSetupStep.COMPLETED) }
            }
        }
    }
}


