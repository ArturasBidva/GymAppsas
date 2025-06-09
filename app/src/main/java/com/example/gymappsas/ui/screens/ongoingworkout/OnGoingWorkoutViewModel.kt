package com.example.gymappsas.ui.screens.ongoingworkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.timer.TimerEvent
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.repository.workout.WorkoutService
import com.example.gymappsas.services.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnGoingWorkoutViewModel @Inject constructor(
    private val workoutService: WorkoutService
) : ViewModel() {

    private val _timerInMillis = MutableLiveData(0L)
    val timerInMillis: LiveData<Long> get() = _timerInMillis

    private val _timerEvent = MutableLiveData<TimerEvent>(TimerEvent.FINISH)
    val timerEvent: LiveData<TimerEvent> get() = _timerEvent

    private val _uiState = MutableStateFlow(OnGoingWorkoutUIState())
    val uiState: StateFlow<OnGoingWorkoutUIState> = _uiState

    init {
        observeTimerEvents()
        observeTimerInMillis()
    }

    private fun observeTimerInMillis() {
        viewModelScope.launch {
            TimerService.timerInMillis.collect { millis ->
                _timerInMillis.value = millis
            }
        }
    }


    fun getOnGoingWorkout(workoutId: Long) {
        viewModelScope.launch {
            workoutService.getAllWorkouts().collect { workouts ->
                val workout = workouts.find { it.id == workoutId }
                if (workout != null) {
                    _uiState.update { state ->
                        state.copy(onGoingWorkout = workout)
                    }
                    if (_uiState.value.currentExercise == null && workout.exerciseWorkouts.isNotEmpty()) {
                        val firstExercise = workout.exerciseWorkouts[0]
                        val nextExercise = if (workout.exerciseWorkouts.size > 1) {
                            workout.exerciseWorkouts[1]
                        } else {
                            null
                        }

                        _uiState.update { state ->
                            state.copy(
                                currentExercise = firstExercise,
                                nextExercise = nextExercise,
                                currentSet = 0,
                                totalSets = firstExercise.goal
                            )
                        }
                    }
                }
            }
        }
    }

    private fun observeTimerEvents() {
        viewModelScope.launch {
            TimerService.timerEvent.collect { event ->
                when (event) {
                    TimerEvent.PAUSED -> isPaused()
                    TimerEvent.RESUMED -> onResume()
                    TimerEvent.EXERCISE -> handleTimerStart()
                    TimerEvent.BREAK -> handleTimerEnd()
                    TimerEvent.FINISH -> workoutCompletionReset()
                    TimerEvent.FORWARD -> onNextSetClicked()
                    TimerEvent.BACKWARD -> onPreviousSetClicked()
                    TimerEvent.DEFAULT -> {
                        //do nothing
                    }
                }
            }
        }
    }

    private fun handleTimerStart() {
        _uiState.update { it.copy(isTimerRunning = true) }
        val phase = if (_uiState.value.currentSet < _uiState.value.totalSets) {
            "Exercise"
        } else {
            "Rest"
        }
        Log.d("OnGoingWorkoutViewModel", "Timer started for $phase phase.")
    }

    private fun handleTimerEnd() {
        val currentExercise = _uiState.value.currentExercise
        if (currentExercise != null) {
            val nextSet = _uiState.value.currentSet + 1
            if (nextSet >= currentExercise.goal) {  // Check if nextSet reaches or exceeds the goal
                moveToNextExerciseOrFinish()  // Move to next exercise or finish workout
            } else {
                _uiState.update {
                    it.copy(
                        currentSet = nextSet,
                        progress = calculateProgress(
                            nextSet.toFloat(),
                            currentExercise.goal.toFloat()
                        )
                    )
                }
                Log.d("OnGoingWorkoutViewModel", "Set $nextSet started.")
            }
        }
    }

    private fun moveToNextExerciseOrFinish() {
        val currentWorkout = _uiState.value.onGoingWorkout
        val currentExerciseIndex = currentWorkout?.exerciseWorkouts?.indexOfFirst {
            it == _uiState.value.currentExercise
        } ?: -1

        if (currentWorkout != null) {
            // If there are more exercises left, move to the next one
            if (currentExerciseIndex + 1 < currentWorkout.exerciseWorkouts.size) {
                transitionToNextExercise(currentExerciseIndex, currentWorkout)
            } else {
                workoutCompletionReset()  // End the workout if no more exercises left
            }
        }
    }

    private fun transitionToNextExercise(currentExerciseIndex: Int, currentWorkout: Workout) {
        val nextExercise = currentWorkout.exerciseWorkouts.getOrNull(currentExerciseIndex + 1)
        val newNextExercise = currentWorkout.exerciseWorkouts.getOrNull(currentExerciseIndex + 2)

        _uiState.update {
            it.copy(
                currentExercise = nextExercise,
                nextExercise = newNextExercise,
                currentSet = 0,
                totalSets = nextExercise?.goal ?: 0,
                progress = 0f
            )
        }
    }

    private fun calculateProgress(currentSet: Float, totalSets: Float): Float {
        if (totalSets == 0f) return 0f
        return (currentSet / totalSets)
    }

    private fun workoutCompletionReset() {
        _uiState.update {
            it.copy(
                currentExercise = null,
                currentSet = 0,
                workoutCompleted = true
            )
        }
    }

    private fun onNextSetClicked() {
        val currentExercise = _uiState.value.currentExercise
        val currentSet = _uiState.value.currentSet

        if (currentExercise != null) {
            val nextSet = currentSet + 1
            if (nextSet <= currentExercise.goal) {
                _uiState.update {
                    it.copy(
                        currentSet = nextSet,
                        progress = calculateProgress(
                            nextSet.toFloat(),
                            currentExercise.goal.toFloat()
                        )
                    )
                }
                Log.d("OnGoingWorkoutViewModel", "Set $nextSet started.")
            } else {
                moveToNextExerciseOrFinish()
            }
        }
    }

    private fun onPreviousSetClicked() {
        val currentExercise = _uiState.value.currentExercise
        val currentSet = _uiState.value.currentSet

        if (currentExercise != null) {
            val previousSet = currentSet - 1
            if (previousSet >= 0) {
                _uiState.update {
                    it.copy(
                        currentSet = previousSet,
                        progress = calculateProgress(
                            previousSet.toFloat(),
                            currentExercise.goal.toFloat()
                        )
                    )
                }
                Log.d("OnGoingWorkoutViewModel", "Set $previousSet started.")
            }
        }
    }

    private fun isPaused() {
        _uiState.update {
            it.copy(
                isPaused = true,
                isTimerRunning = false
            )
        }
    }

    private fun onResume() {
        _uiState.update {
            it.copy(
                isPaused = false,
                isTimerRunning = true,

            )
        }
    }

}
