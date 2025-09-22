package com.example.gymappsas.ui.screens.workoutprep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.WorkoutVariant
import com.example.gymappsas.data.repository.completedworkout.CompletedWorkoutsRepository
import com.example.gymappsas.data.repository.workout.WorkoutService
import com.example.gymappsas.data.repository.workoutvariants.WorkoutVariantsRepository
import com.example.gymappsas.util.TimerUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutPreparationViewModel @Inject constructor(
    private val workoutService: WorkoutService,
    private val completedWorkoutsRepository: CompletedWorkoutsRepository,
    private val workoutVariantsRepository: WorkoutVariantsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutPreparationUiState())
    val uiState: StateFlow<WorkoutPreparationUiState> = _uiState


    fun getWorkoutById(workoutId: Long) {
        viewModelScope.launch {
            workoutService.getAllWorkouts().collect { workouts ->
                val workout = workouts.find { it.id == workoutId }
                _uiState.update {
                    it.copy(
                        selectedWorkout = workout,
                        selectedMethod = workout?.trainingMethod?.let { methodName ->
                            TrainingMethod.valueOf(methodName)
                        } ?: TrainingMethod.PYRAMID
                    )
                }
            }
        }
    }


    fun updateExerciseWorkoutData(exerciseWorkout: ExerciseWorkout) {
        viewModelScope.launch {
            workoutService.exerciseWorkoutService.updateExerciseWorkoutData(exerciseWorkout)

            _uiState.update { state ->
                state.selectedWorkout?.let { workout ->
                    val updatedExercises = workout.exerciseWorkouts.map {
                        if (it.id == exerciseWorkout.id) exerciseWorkout else it
                    }
                    state.copy(
                        selectedWorkout = workout.copy(exerciseWorkouts = updatedExercises)
                    )
                } ?: state
            }
        }
    }

    fun updateRestTime(value: Int) {
        _uiState.update { it.copy(restTime = value) }
    }
    fun updateCustomRestTime(value: String) {
        _uiState.update { it.copy(customRestTime = value) }
    }

    private fun ifAnyTimeIsSelected(): Boolean {
        val restTime = uiState.value.restTime
        val customTimeStr = uiState.value.customRestTime
        val customTime = customTimeStr.toIntOrNull() ?: 0

        return restTime > 0 || customTime > 0
    }

    fun updateStep() {
        _uiState.update { it.copy(currentStep = uiState.value.currentStep + 1) }
    }

    fun updateStepBack() {
        _uiState.update { it.copy(currentStep = uiState.value.currentStep - 1) }
    }

    fun isNextStepDisabled(): Boolean {
        val selectedWorkout = uiState.value.selectedWorkout
        return if (selectedWorkout != null) {
            when (uiState.value.currentStep) {
                1 -> false
                2 -> !ifAnyTimeIsSelected()
                3 -> selectedWorkout.exerciseWorkouts.any { exercise ->
                    exercise.maxWeight <= 0
                }
                else -> false
            }
        } else {
            true
        }
    }

    fun isSaveDisabled(): Boolean {
        return uiState.value.workoutVariantName.isBlank()
    }

    fun selectTrainingMethod(method: TrainingMethod) {
        _uiState.update { it.copy(selectedMethod = method) }
    }

    fun onEditWeights(){
        _uiState.update { it.copy(currentStep = 3) }
    }

    fun updateWorkoutVariantName(name: String) {
        _uiState.update { it.copy(workoutVariantName = name) }
    }

    fun saveWorkout() {
        viewModelScope.launch {
            val selectedWorkout = uiState.value.selectedWorkout
            val workoutVariantName = uiState.value.workoutVariantName
            val selectedMethod = uiState.value.selectedMethod
            val restTime = if (uiState.value.restTime == -1) {
                uiState.value.customRestTime.toIntOrNull() ?: 60
            } else {
                uiState.value.restTime
            }


            if (selectedWorkout != null && workoutVariantName.isNotBlank()) {
                val estimatedDurationSeconds = selectedWorkout.exerciseWorkouts.sumOf {
                    it.duration + it.breakTime
                }

                val estimatedDurationMinutes = estimatedDurationSeconds / 60
                // Create workout variant
                val workoutVariant = WorkoutVariant(
                    id = selectedWorkout.id,
                    name = workoutVariantName,
                    trainingMethod = selectedMethod.displayName,
                    restTimeSeconds = restTime,
                    exercises = selectedWorkout.exerciseWorkouts,
                    createdAt = TimerUtil.getFormattedTime(System.currentTimeMillis()),
                    estimatedDuration = estimatedDurationMinutes,
                    isFavourite = selectedWorkout.isFavorite
                )

                // Save the variant
                workoutVariantsRepository.saveWorkoutVariant(workoutVariant)
                _uiState.update { it.copy(workoutSaved = true) }
            }
        }
    }
}
