package com.example.gymappsas.ui.screens.startworkout

import androidx.lifecycle.ViewModel
import com.example.gymappsas.data.repository.completedworkout.CompletedWorkoutsService
import com.example.gymappsas.data.repository.workout.WorkoutService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class StartWorkoutViewModel @Inject constructor(
    private val workoutService: WorkoutService,
    private val completedWorkoutsService: CompletedWorkoutsService
) : ViewModel() {
    private val _uiState: MutableStateFlow<StartWorkoutUiState> =
        MutableStateFlow(StartWorkoutUiState())
    val uiState: StateFlow<StartWorkoutUiState> = _uiState


    }