package com.example.gymappsas.ui.screens.createworkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.db.models.workouts.WorkoutCategory
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import com.example.gymappsas.data.repository.workout.WorkoutService
import com.example.gymappsas.util.CreateWorkoutDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    private val createWorkoutDataValidator: CreateWorkoutDataValidator,
    private val workoutService: WorkoutService,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateWorkoutUiState())
    val uiState: StateFlow<CreateWorkoutUiState> = _uiState

    init {
        getExerciseCategories()
        getAllExercises()
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

    fun updateCategory(category: WorkoutCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
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

    private fun getAllExercises() {
        viewModelScope.launch {
            exerciseRepository.exercises.onStart {
                _uiState.update { it.copy(isLoading = true) }
            }.collect { exercises ->
                _uiState.update { it.copy(exercises = exercises, isLoading = false) }
            }
        }
    }

    private fun validateFields(): Boolean {
        validateTitleField()
        validateDescriptionField()
        return !(_uiState.value.hasTitleError || _uiState.value.hasDescriptionError)
    }

    fun navigateToAddExerciseToWorkout() {
        if (validateFields()) {
            _uiState.update { it.copy(createWorkoutStep = CreateWorkoutStep.ADDEXERCISES) }
        }
    }

    fun createWorkout() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
           val workoutId = workoutService.createWorkoutWithExercises(
                workout = Workout(
                    title = uiState.value.workoutTitle,
                    description = uiState.value.workoutDescription,
                    category = uiState.value.selectedCategory,
                    exerciseWorkouts = _uiState.value.selectedExercises.map { exercise ->
                        ExerciseWorkout(
                            exercise = exercise
                        )
                    }
                ))
            if(workoutId != null){
               _uiState.update {
                it.copy(
                    createdWorkoutId = workoutId,
                    hasNavigated = true,
                    createWorkoutStep = CreateWorkoutStep.COMPLETE,
                    isLoading = false
                )
               }
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