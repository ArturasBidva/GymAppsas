package com.example.gymappsas.ui.screens.mainscreen

import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.data.repository.fitness.FitnessData

data class MainScreenUiState(
    val isLoading: Boolean = true,
    val profile : Profile = Profile(),
    val completedWorkoutsWeeklyCounter: Int = 0,
    val todayFitnessData: FitnessData? = null
)