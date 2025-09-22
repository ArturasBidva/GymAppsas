package com.example.gymappsas.ui.screens.exercisedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gymappsas.ExerciseDetailsScreen
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseDetailsUiState())
    val uiState: StateFlow<ExerciseDetailsUiState> = _uiState
    private val route = savedStateHandle.toRoute<ExerciseDetailsScreen>()
    private val exerciseId: Long = route.exerciseId

    init {
        loadExercise()
    }


    private fun loadExercise() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val selectedExercise =
                exerciseRepository.exercises.value.firstOrNull { it.id == exerciseId }
            _uiState.update { it.copy(selectedExercise = selectedExercise, isLoading = false) }
            if (selectedExercise != null) {
                exerciseRepository.addToRecentlyViewed(exercise = selectedExercise)
            }

        }
    }

}