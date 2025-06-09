package com.example.gymappsas.ui.screens.createworkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.repository.workout.WorkoutService
import com.example.gymappsas.util.CreateWorkoutDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    private val createWorkoutDataValidator: CreateWorkoutDataValidator,
    private val workoutService: WorkoutService
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateWorkoutUiState())
    val uiState: StateFlow<CreateWorkoutUiState> = _uiState

    init {
        getExerciseCategories()
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

    private fun getExerciseCategories() {
        _uiState.update { it.copy(exerciseCategories = getAllCategories()) }
    }

    fun getSelectedCategories(categories: List<String>) {
        _uiState.update { it.copy(selectedCategories = categories) }
    }

    fun updateTitle(title: String) {
        _uiState.update { it.copy(workoutTitle = title) }
        validateTitleField()
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(workoutDescription = description) }
        validateDescriptionField()
    }

    private fun validateTitleField() {
        val currentState = uiState.value

        val titleError = if (currentState.workoutTitle.isBlank()) {
            createWorkoutDataValidator.validateTitle(currentState.workoutTitle)
        } else null
        _uiState.update {
            it.copy(
                hasTitleError = titleError != null,
                titleErrorMessage = titleError?.toMessage() ?: "",
            )
        }
    }

    private fun validateDescriptionField() {
        val currentState = uiState.value
        val descriptionError = if (currentState.workoutDescription.isBlank()) {
            createWorkoutDataValidator.validateDescription(currentState.workoutDescription)
        } else null

        _uiState.update {
            it.copy(
                hasDescriptionError = descriptionError != null,
                descriptionErrorMessage = descriptionError?.toMessage() ?: "",
            )
        }
    }

    fun validateFields() {
        validateTitleField()
        validateDescriptionField()
    }

    fun createWorkout(workout: Workout) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
           val workoutId = workoutService.createWorkoutWithExercises(workout = workout)
            _uiState.update { it.copy(createdWorkoutId = workoutId, isLoading = false, hasNavigated = false) }
        }
    }
    fun setNavigated() {
        _uiState.update { it.copy(hasNavigated = true) }
    }
}