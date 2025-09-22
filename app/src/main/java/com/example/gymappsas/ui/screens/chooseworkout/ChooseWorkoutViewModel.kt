package com.example.gymappsas.ui.screens.chooseworkout

import androidx.lifecycle.ViewModel
import com.example.gymappsas.data.repository.workout.WorkoutService

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseWorkoutViewModel @Inject constructor(
private val workoutService: WorkoutService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseWorkoutUiState())
    val uiState: StateFlow<ChooseWorkoutUiState> = _uiState

    init {
        getAllWorkouts()
    }
    private fun getAllWorkouts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                workoutService.getAllWorkouts()
                    .collect { items ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                workouts = items,
                            )
                        }
                    }
            } catch (e: Exception) {

                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    fun selectWorkout(workoutId: Long) {
        val selectedWorkout = uiState.value.workouts.find { it.id == workoutId }
        _uiState.update { it.copy(chosenWorkout = selectedWorkout) }
    }

    fun checkIfWorkoutReadyToStart(): Boolean {
        return uiState.value.chosenWorkout?.exerciseWorkouts?.let { exercises ->
            exercises.all { it.maxWeight > 0 }
        } ?: false
    }
}