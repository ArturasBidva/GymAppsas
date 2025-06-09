package com.example.gymappsas.ui.screens.workoutprep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.repository.workout.WorkoutService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutPreparationViewModel @Inject constructor(
    private val workoutService: WorkoutService
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutPreparationUiState())
    val uiState: StateFlow<WorkoutPreparationUiState> = _uiState


    fun getWorkoutById(workoutId: Long) {
        viewModelScope.launch {
            workoutService.getAllWorkouts().collect { items ->
                val workout = items.find { it.id == workoutId }
                _uiState.update {
                    it.copy(selectedWorkout = workout)
                }
            }
        }
    }

    fun toggleOffSetExerciseWeightDialog() {
        _uiState.update { it.copy(isSelectWeightDialogOpen = false) }
    }

    fun getSelectedWeight(weight: String) {
        _uiState.update { it.copy(selectedWeight = weight) }
    }

    fun updateExerciseWorkoutWeight() {
        viewModelScope.launch {
            uiState.value.selectedExerciseWorkoutId?.let { selectedExerciseWorkoutId ->
                workoutService.exerciseWorkoutService.updateExerciseWorkoutWeight(
                    exerciseWorkoutId = selectedExerciseWorkoutId,
                    weight = uiState.value.selectedWeight.toInt()
                )
                _uiState.update { it.copy(isSelectWeightDialogOpen = false, progress = it.progress + 1) }
            }
        }
    }

    fun getSelectedExerciseWorkout(exerciseWorkoutId: Long) {
        _uiState.update {
            it.copy(selectedExerciseWorkoutId = exerciseWorkoutId, isSelectWeightDialogOpen = true)
        }
    }
}
