package com.example.gymappsas.ui.screens.workouthistory

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WorkoutHistoryViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutHistoryUiState())
    val uiState: StateFlow<WorkoutHistoryUiState> = _uiState

    init {
        getCompletedWorkouts()
    }

    private fun getCompletedWorkouts() {

    }
}