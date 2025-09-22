package com.example.gymappsas.ui.screens.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.completedworkout.CompletedWorkout
import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.data.repository.completedworkout.CompletedWorkoutsService
import com.example.gymappsas.data.repository.exercise.ExerciseRepository
import com.example.gymappsas.data.repository.fitness.FitnessRepository
import com.example.gymappsas.data.repository.profile.ProfileService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val completedWorkoutsService: CompletedWorkoutsService,
    private val profileService: ProfileService,
    private val fitnessRepository: FitnessRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState

    init {
        viewModelScope.launch {
            getUserProfile()
            exerciseRepository.loadExercisesToStateFlow()
            _uiState.update { it.copy(isLoading = true) }
            val completedWorkoutsFlow = completedWorkoutsService.getCompletedWorkouts()
            completedWorkoutsFlow.collect { completedWorkouts ->
                val weeklyCounter = getWeeklyWorkoutCounter(completedWorkouts)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        completedWorkoutsWeeklyCounter = weeklyCounter
                    )
                }
            }
        }
    }

    private fun loadFitnessData(profile: Profile) {
        viewModelScope.launch {
            fitnessRepository.getTodayFitnessData(profile).collect { todayFitnessData ->
                _uiState.update { it.copy(todayFitnessData = todayFitnessData) }
            }
        }
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            profileService.getProfile().onStart {
                _uiState.update { it.copy(isLoading = true) }
            }.collect { profile ->
                if (profile != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            profile = profile,
                        )
                    }
                    loadFitnessData(profile)
                }
            }
        }
}

private fun getStartAndEndOfWeek(): Pair<LocalDate, LocalDate> {
    val today = LocalDate.now()
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    return Pair(startOfWeek, endOfWeek)
}


private fun getWeeklyWorkoutCounter(workouts: List<CompletedWorkout>): Int {
    val (startOfWeek, endOfWeek) = getStartAndEndOfWeek()
    return workouts.count {
        it.completedDate.isAfter(startOfWeek.minusDays(1)) && it.completedDate.isBefore(
            endOfWeek.plusDays(1)
        )
    }
}

fun ifYouHaveWorkoutToday(): Boolean {
    val today = LocalDate.now()
    return _uiState.value.profile.workoutDays.find { it == today.dayOfWeek.value.toString() } != null
}

fun ifYouHaveWorkouts(): Boolean {
    return _uiState.value.profile.workouts.isNotEmpty()
}
}