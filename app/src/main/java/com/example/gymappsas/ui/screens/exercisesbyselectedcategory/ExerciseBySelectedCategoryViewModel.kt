package com.example.gymappsas.ui.screens.exercisesbyselectedcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExerciseBySelectedCategoryViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExerciseBySelectedCategoryUiState())
    val uiState: StateFlow<ExerciseBySelectedCategoryUiState> = _uiState


    fun getAllExercisesBySelectedCategory(categoryName: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val exercises = exerciseRepository.exercises.value
            val filteredExercises = exercises.filter { exercise -> exercise.primaryMuscles.any { it == categoryName.lowercase(
                Locale.ROOT
            ) } }

            _uiState.update {
                it.copy(exercisesByCategory = filteredExercises, isLoading = false)
            }
        }
    }

    private fun getExercisesBySelectedCategory() {
        _uiState.update { it.copy(isLoading = true) }
    }
}