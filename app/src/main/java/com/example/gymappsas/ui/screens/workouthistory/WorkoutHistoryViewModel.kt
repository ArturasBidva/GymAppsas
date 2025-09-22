package com.example.gymappsas.ui.screens.workouthistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.repository.completedworkout.CompletedWorkoutsRepository
import com.example.gymappsas.data.repository.workout.WorkoutRepository
import com.example.gymappsas.data.repository.workoutvariants.WorkoutVariantsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutHistoryViewModel @Inject constructor(
    private val completedWorkoutsRepository: CompletedWorkoutsRepository,
    private val workoutRepository: WorkoutRepository,
    private val workoutVariantsRepository: WorkoutVariantsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutHistoryUiState())
    val uiState: StateFlow<WorkoutHistoryUiState> = _uiState.asStateFlow()

    init {
        //getCompletedWorkouts()
    }

    fun loadWorkoutHistory(workoutId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Load workout details - note: this returns WorkoutEntity, may need conversion
                val workoutEntity = workoutRepository.getWorkoutByID(workoutId)

                // Combine completed workouts and workout variants flows
                combine(
                    completedWorkoutsRepository.getCompletedWorkoutsByWorkoutId(workoutId),
                    workoutVariantsRepository.getWorkoutVariantsByWorkoutId(workoutId)
                ) { completedWorkouts, workoutVariants ->
                    Pair(completedWorkouts, workoutVariants)
                }.collect { (completedWorkouts, workoutVariants) ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        workout = null, // TODO: Convert WorkoutEntity to Workout model
                        completedWorkouts = completedWorkouts.map { entity ->
                            // Convert entity to model if needed
                            com.example.gymappsas.data.db.models.completedworkout.CompletedWorkout(
                                id = entity.id,
                                workout = com.example.gymappsas.data.db.models.workouts.Workout(
                                    id = entity.workoutId,
                                    title = entity.workoutTitle,
                                    description = "",
                                    category = com.example.gymappsas.data.db.models.workouts.WorkoutCategory.STRENGTH,
                                    exerciseWorkouts = emptyList()
                                ),
                                completedDate = entity.completedDate,
                                durationMinutes = entity.durationMinutes,
                                maxWeight = entity.maxWeight,
                                notes = entity.notes
                            )
                        },
                        workoutVariants = workoutVariants
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}