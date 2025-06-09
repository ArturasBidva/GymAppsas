package com.example.gymappsas.ui.screens.workoutschedule

import BaseScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.ui.reusable.components.AddWorkoutToSchedule
import com.example.gymappsas.ui.reusable.components.SimpleCalendarTitle
import com.example.gymappsas.ui.screens.workout.WorkoutViewModel
import com.example.gymappsas.util.MockSchedulesData
import com.example.gymappsas.util.WorkoutScheduleEvents
import com.example.gymappsas.util.rememberFirstMostVisibleMonth
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun WorkoutScheduleScreen(
    viewModel: WorkoutScheduleViewModel,
    workoutViewModel: WorkoutViewModel = hiltViewModel(),
    popStackBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val workoutUiState by workoutViewModel.uiState.collectAsState()
    val context = LocalContext.current

    BaseScreen(isLoading = uiState.isLoading, topBarTitle = "Workout schedule") { snackBarHostState ->

        LaunchedEffect(key1 = snackBarHostState) {
                viewModel.events.collect { event ->
                    when (event) {
                        is WorkoutScheduleEvents.None -> {
                            //Do nothing
                        }

                        is WorkoutScheduleEvents.Error -> {
                            snackBarHostState.showSnackbar(event.error.asString(context = context))
                        }
                    }
                }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Content(
                workoutScheduleUiState = uiState,
                deleteSchedule = viewModel::deleteSchedule,
                onDaySelection = viewModel::onCalendarDateSelection,
                onEditScheduleSelect = viewModel::onEditScheduleSelect,
                popStackBack = { popStackBack() }
            )
        }

        AddWorkoutToSchedule(
            workoutUiState = workoutUiState,
            workoutScheduleUiState = uiState,
            onWorkoutSelect = viewModel::onWorkoutSelect,
            workoutScheduleDialogVisibility = viewModel::setWorkoutScheduleDialogVisibility,
            workoutScheduleDateDialogVisibility = viewModel::setWorkoutScheduleDateDialogVisibility,
            createWorkoutSchedule = viewModel::createSchedule,
            updateSchedule = viewModel::updateSchedule,
            onTimeConfirm = viewModel::onTimeConfirmation,
            onTimeValidation = viewModel::validateSelectedTime,
            onTimePickerDismiss = viewModel::onTimePickerDismiss,
            onOpenTimePickerClick = viewModel::onOpenTimePickerClick,
            isWorkoutScheduledOnSameDay = viewModel::isWorkoutScheduledOnSameDay,
            visible = uiState.isDialogVisible || uiState.isEditMode,
        )
    }
}

@Composable
private fun Content(
    workoutScheduleUiState: WorkoutScheduleUiState,
    deleteSchedule: (LocalDate, Long) -> Unit,
    onEditScheduleSelect: (Schedule) -> Unit,
    onDaySelection: (LocalDate?) -> Unit,
    popStackBack: () -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    val daysOfWeek = remember { daysOfWeek() }
    val listState = rememberLazyListState()

    Surface(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp + 56.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            BannerSection()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .background(Color.White),
            ) {
                val calendarState = rememberCalendarState(
                    startMonth = startMonth,
                    endMonth = endMonth,
                    firstVisibleMonth = currentMonth,
                    firstDayOfWeek = daysOfWeek.first(),
                )
                val coroutineScope = rememberCoroutineScope()
                val visibleMonth =
                    rememberFirstMostVisibleMonth(calendarState, viewportPercent = 90f)
                LaunchedEffect(visibleMonth) {
                    // Clear selection if we scroll to a new month.
                    onDaySelection(null)
                }
                SimpleCalendarTitle(
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                    currentMonth = visibleMonth.yearMonth,
                    goToPrevious = {
                        coroutineScope.launch {
                            calendarState.animateScrollToMonth(
                                calendarState.firstVisibleMonth.yearMonth.previousMonth
                            )
                        }
                    },
                    goToNext = {
                        coroutineScope.launch {
                            calendarState.animateScrollToMonth(
                                calendarState.firstVisibleMonth.yearMonth.nextMonth
                            )
                        }
                    },
                )
                WorkoutScheduleCalendar(
                    workoutScheduleUiState = workoutScheduleUiState,
                    calendarState = calendarState,
                    onDaySelection = {
                        onDaySelection(it.date)
                    },
                    daysOfWeek = daysOfWeek,
                    schedules = workoutScheduleUiState.schedules
                )
                Spacer(modifier = Modifier.height(10.dp))
                val selectedDateSchedules =
                    workoutScheduleUiState.selectedCalendarDate?.let { selectedDate ->
                        workoutScheduleUiState.schedules.filter {
                            it.date == selectedDate
                        }
                    } ?: workoutScheduleUiState.schedules
                LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                    items(items = selectedDateSchedules) {

                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun WorkoutSchedulePreview() {
    val mockSchedules = MockSchedulesData.mockSchedules
    val workoutScheduleUiState = WorkoutScheduleUiState(schedules = mockSchedules)

    Content(
        workoutScheduleUiState = workoutScheduleUiState,
        deleteSchedule = { date, workoutId ->
            println("Deleting schedule for date: $date, workoutId: $workoutId")
        },
        onEditScheduleSelect = {},
        onDaySelection = {},
        popStackBack = {},
    )
}

@Composable
private fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 16.dp) // Apply padding to Box, not the image
            .clip(RoundedCornerShape(16.dp)) // Clip the entire Box
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.workoutschedule),
            contentDescription = null,
            contentScale = ContentScale.FillBounds, // Fit the image without cropping
            modifier = Modifier.fillMaxSize() // Image takes up the full Box size
        )
    }
}
