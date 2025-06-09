package com.example.gymappsas.ui.screens.addexercisetoworkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExerciseToWorkoutViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddExerciseToWorkoutUiState())
    val uiState: StateFlow<AddExerciseToWorkoutUiState> = _uiState

    init {
        getAllExercises()
    }

    private fun getAllExercises() {
        viewModelScope.launch {
            exerciseRepository.exercises.onStart {
                _uiState.update { it.copy(isLoading = true) }
            }.collect { exercises ->
                _uiState.update { it.copy(exercises = exercises, isLoading = false) }
            }
        }
    }

    fun toggleExerciseSelection(exercise: Exercise) {
        val currentExercises = _uiState.value.selectedExercises
        val updatedExercises = if (currentExercises.contains(exercise)) {
            currentExercises - exercise
        } else {
            currentExercises + exercise
        }

        _uiState.value = _uiState.value.copy(selectedExercises = updatedExercises)
    }
    }

