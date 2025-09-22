package com.example.gymappsas.ui.screens.exercisesbycategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseUiState())
    val uiState: StateFlow<ExerciseUiState> = _uiState

    init {
        getExerciseCategories()
        getExercises()
        getRecentlyViewedExercises()
    }

    private fun getExerciseCategories() {
        _uiState.update { it.copy(exerciseCategories = getAllCategories()) }
    }
    private fun getAllCategories(): List<ExerciseCategory> {
        return listOf(
            ExerciseCategory.Biceps,
            ExerciseCategory.Lats,
            ExerciseCategory.Abdominals,
            ExerciseCategory.Glutes, ExerciseCategory.Quadriceps,
            ExerciseCategory.Shoulders, ExerciseCategory.Triceps, ExerciseCategory.Chest
        )
    }

    private fun getRecentlyViewedExercises() {
        viewModelScope.launch {
            exerciseRepository.recentlyViewedExercises.collect { recentList ->
                _uiState.update { it.copy(recentViewedExercises = recentList) }
            }
        }
    }

    fun clearRecentlyViewed() {
        viewModelScope.launch {
            exerciseRepository.clearRecentlyViewedExercises()
            _uiState.update { it.copy(recentViewedExercises = emptyList()) }
        }
    }

    fun isExpanded(expanded: Boolean) {
        viewModelScope.launch { _uiState.update { it.copy(isExpanded = expanded) } }
    }

    private fun getExercises() {
        viewModelScope.launch {
           exerciseRepository.exercises
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
                .collect { items ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            exercises = items
                        )
                    }
                }
        }
    }
}
