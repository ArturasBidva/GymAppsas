package com.example.gymappsas.ui.screens.exercisedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseDetailsUiState())
    val uiState: StateFlow<ExerciseDetailsUiState> = _uiState

    fun getSelectedExercise(exerciseId : Long) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val selectedExercise = exerciseRepository.exercises.value.first { it.id == exerciseId }
            _uiState.update { it.copy(selectedExercise = selectedExercise, isLoading = false) }
        }
    }

}