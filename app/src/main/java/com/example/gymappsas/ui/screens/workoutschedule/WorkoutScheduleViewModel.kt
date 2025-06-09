package com.example.gymappsas.ui.screens.workoutschedule

import TimeValidator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.repository.schedule.ScheduleService
import com.example.gymappsas.util.WorkoutScheduleDataValidator
import com.example.gymappsas.util.WorkoutScheduleEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WorkoutScheduleViewModel @Inject constructor(
    private val scheduleService: ScheduleService,
    private val workoutScheduleDataValidator: WorkoutScheduleDataValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutScheduleUiState())
    val uiState: StateFlow<WorkoutScheduleUiState> = _uiState
    private val eventChannel = Channel<WorkoutScheduleEvents>()
    val events = eventChannel.receiveAsFlow()

    init {
        getAllSchedules()
    }

    private fun getAllSchedules() {
        viewModelScope.launch {
            scheduleService.getAllSchedules()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { schedules ->
                    _uiState.update {
                        it.copy(schedules = schedules, isLoading = false)
                    }
                }
        }
    }

    fun createSchedule() {
        viewModelScope.launch {
            val workoutScheduleUiState = uiState.value
            val schedule = Schedule(
                workout = workoutScheduleUiState.selectedWorkout,
                startTime = workoutScheduleUiState.schedule.startTime,
                endTime = workoutScheduleUiState.schedule.endTime,
                date = workoutScheduleUiState.selectedCalendarDate ?: workoutScheduleUiState.schedule.date,
                color = workoutScheduleUiState.schedule.color,
                note = workoutScheduleUiState.schedule.note
            )

            if (isFieldsHasNoError()) {
                scheduleService.insertSchedule(schedule = schedule)
                _uiState.update { it.copy(isDialogVisible = false, isEditMode = false) }
            }
        }
    }

    private fun validateFields() {
        val workoutScheduleUiState = uiState.value
        workoutScheduleDataValidator.validateTimes(
            startTime = workoutScheduleUiState.schedule.startTime,
            endTime = workoutScheduleUiState.schedule.endTime
        )
        workoutScheduleDataValidator.validateWorkout(
            workoutLocal = workoutScheduleUiState.selectedWorkout
        )

        val fieldErrors = WorkoutScheduleFieldErrors(
//            startTimeError = results.first is Result.Error,
//            endTimeError = results.second is Result.Error,
//            workoutSelectError = workoutSelection is Result.Error
        )

        _uiState.update {
            it.copy(fieldErrors = fieldErrors)
        }
    }

    private fun isFieldsHasNoError(): Boolean {
        validateFields()
        return !uiState.value.fieldErrors.hasErrors()
    }

    fun setWorkoutScheduleDialogVisibility(visible: Boolean) {
        _uiState.update {
            val scheduleWithDate =
                uiState.value.schedule.copy(
                    date = uiState.value.selectedCalendarDate ?: LocalDate.now()
                )
            it.copy(
                isDialogVisible = visible,
                isEditMode = false,
                schedule = scheduleWithDate,
                selectedWorkout = if (!visible) Workout() else uiState.value.selectedWorkout
            )
        }
    }


    fun isWorkoutScheduledOnSameDay(workout: Workout, date: LocalDate): Boolean {
        return uiState.value.schedules.any { it.workout.id == workout.id && it.date == date }
    }

    fun onWorkoutSelect(workout: Workout) {
        val date = uiState.value.selectedCalendarDate
        if (date == null) {
            return
        } else {
            _uiState.update { it.copy(selectedWorkout = workout) }
        }
    }


    fun setWorkoutScheduleDateDialogVisibility(visible: Boolean) {
        _uiState.update {
            it.copy(isCalendarDialogVisible = visible)
        }
    }

    fun deleteSchedule(date: LocalDate, workoutId: Long) {
        viewModelScope.launch {
            scheduleService.deleteScheduleById(date = date, workoutId = workoutId)
        }
    }

    fun updateSchedule(schedule: Schedule) {
        _uiState.update { it.copy(schedule = schedule) }
    }

    fun validateSelectedTime(time: Long): Boolean {
        val previousDayMillis = Date().time - TimeUnit.DAYS.toMillis(1L)
        return Date(time).after(Date(previousDayMillis))
    }

    fun onCalendarDateSelection(date: LocalDate?) {
        _uiState.update { it.copy(selectedCalendarDate = date) }
    }

    fun onEditScheduleSelect(schedule: Schedule) {
        _uiState.update {
            it.copy(
                schedule = schedule,
                isDialogVisible = true,
                isEditMode = true,
                selectedWorkout = schedule.workout
            )
        }
    }

    fun onTimeConfirmation(time: LocalTime) {
        val schedule = uiState.value.schedule
        uiState.value.timeSelectionDialogType?.let { type ->
            when (type) {
                is TimeSelectionDialogType.StartTime -> {
                    if (TimeValidator.validateStartTime(
                            startTime = time,
                            endTime = uiState.value.schedule.endTime
                        )
                    ) {
                        _uiState.update {
                            val scheduleWithStartTime = schedule.copy(startTime = time)
                            it.copy(
                                schedule = scheduleWithStartTime,
                                timeSelectionDialogType = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                schedule = schedule.copy(startTime = null),
                                timeSelectionDialogType = null
                            )
                        }
                    }
                }

                is TimeSelectionDialogType.EndTime -> {
                    val scheduleWithEndTime = schedule.copy(endTime = time)
                    val isValidEndTime = TimeValidator.validateEndTime(
                        startTime = uiState.value.schedule.startTime,
                        endTime = time
                    )

                    if (isValidEndTime) {
                        _uiState.update {
                            it.copy(
                                schedule = scheduleWithEndTime,
                                timeSelectionDialogType = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                schedule = schedule.copy(endTime = null),
                                timeSelectionDialogType = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun onTimePickerDismiss() {
        _uiState.update { it.copy(timeSelectionDialogType = null) }
    }

    fun onOpenTimePickerClick(dialogType: TimeSelectionDialogType) {
        _uiState.update { it.copy(timeSelectionDialogType = dialogType) }
    }
}