package com.example.gymappsas.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.R
import com.example.gymappsas.data.repository.profile.ProfileService
import com.example.gymappsas.data.repository.workout.WorkoutService
import com.example.gymappsas.util.Resource
import com.example.gymappsas.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutService: WorkoutService,
    private val profileService: ProfileService
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState

    init {
        getAllWorkouts()
        getProfile()
    }

    fun updateSearchText(newText: String) {
        _uiState.update { currentState ->
            val filtered = if (newText.isBlank()) {
                currentState.workouts
            } else {
                currentState.workouts.filter {
                    it.doesMatchSearchQuery(newText)
                }
            }
            currentState.copy(
                searchText = newText,
                filteredWorkouts = filtered
            )
        }
    }

    fun selectedWorkout(workoutId: Long) {
        val workout = uiState.value.workouts.first { it.id == workoutId }
        _uiState.update { it.copy(selectedWorkout = workout) }
    }

    fun markVariantAsUsed(variantId: Long) {
        viewModelScope.launch {
            try {
                workoutService.markVariantAsUsed(variantId)
                // Refresh the data after marking as used
                getAllWorkouts()
            } catch (e: Exception) {
                _uiState.update { it.copy(uiText = UiText.DynamicString(e.message.toString())) }
            }
        }
    }

    private fun getAllWorkouts(onFetchComplete: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                workoutService.getAllWorkouts()
                    .collect { items ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                workouts = items,
                                selectedWorkout = items.firstOrNull(),
                                filteredWorkouts = items,
                            )
                        }
                        onFetchComplete()
                    }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteWorkoutById(workoutId: Long): Resource<Unit> {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                workoutService.deleteWorkoutById(workoutId = workoutId)
                Resource.Success(null)
            } catch (e: Exception) {
                Resource.Error(UiText.DynamicString(e.message.toString()))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
        return Resource.Success(null)
    }

    private fun getProfile() {
        viewModelScope.launch {
            try {
                profileService.getProfile().collect { profile ->
                    _uiState.update { it.copy(profile = profile) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(uiText = UiText.DynamicString(e.message.toString())) }
            }
        }
    }

    fun addWorkoutToFavourites(workoutId: Long) {
        viewModelScope.launch {
            try {
               val result = workoutService.addWorkoutToFavourites(workoutId = workoutId)
                if(result is Resource.Success){
                    _uiState.update { it.copy(uiText = UiText.StringResource(R.string.workout_added_to_favourites)) }
                    getAllWorkouts()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(uiText = UiText.DynamicString(e.message.toString())) }
            }
        }
    }

    fun removeWorkoutFromFavourites(workoutId: Long) {
        viewModelScope.launch {
            try {
                val result = workoutService.removeWorkoutFromFavourites(workoutId = workoutId)
                if(result is Resource.Success) {
                    _uiState.update { it.copy(uiText = UiText.StringResource(R.string.workout_removed_from_favourites)) }
                    getAllWorkouts()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(uiText = UiText.DynamicString(e.message.toString())) }
            }
        }
    }

    fun clearUiText() {
        _uiState.update { it.copy(uiText = null) }
    }
}
