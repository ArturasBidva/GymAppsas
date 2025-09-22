package com.example.gymappsas.ui.screens.exercisesbyselectedcategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.gymappsas.ExerciseBySelectedCategoryScreen
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseBySelectedCategoryViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseBySelectedCategoryUiState())
    val uiState: StateFlow<ExerciseBySelectedCategoryUiState> = _uiState


    private val route = savedStateHandle.toRoute<ExerciseBySelectedCategoryScreen>()
    init {
        getAllExercisesBySelectedCategory(route.exerciseCategory)
    }

    val category = route.exerciseCategory

    private fun getAllExercisesBySelectedCategory(category: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val exercises = exerciseRepository.exercises.value
            val filteredExercises = exercises.filter { exercise ->
                exercise.primaryMuscles.any { it.equals(category, ignoreCase = true) }
            }

            _uiState.update {
                it.copy(exercisesByCategory = filteredExercises, isLoading = false)
            }
        }
    }
}