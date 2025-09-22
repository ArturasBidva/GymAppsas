package com.example.gymappsas.ui.screens.workoutschedule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.ui.reusable.components.AddWorkoutToSchedule
import com.example.gymappsas.ui.reusable.components.SimpleCalendarTitle
import com.example.gymappsas.ui.screens.workout.WorkoutViewModel
import com.example.gymappsas.util.MockSchedulesData
import com.example.gymappsas.util.rememberFirstMostVisibleMonth
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutScheduleScreen(
    viewModel: WorkoutScheduleViewModel,
    workoutViewModel: WorkoutViewModel = hiltViewModel(),
    popStackBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val workoutUiState by workoutViewModel.uiState.collectAsState()
    val context = LocalContext.current

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BannerSection()

            // White container for calendar and events
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color.White)
                    .padding(16.dp)
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

                // Events section
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Upcoming Workouts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))

                val selectedDateSchedules =
                    workoutScheduleUiState.selectedCalendarDate?.let { selectedDate ->
                        workoutScheduleUiState.schedules.filter {
                            it.date == selectedDate
                        }
                    } ?: workoutScheduleUiState.schedules

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = selectedDateSchedules) { schedule ->
                        WorkoutEventCard(
                            schedule = schedule,
                            onStartClick = { /* implement start click */ },
                            onViewClick = { /* implement view click */ },
                            onRescheduleClick = {
                                deleteSchedule(
                                    schedule.date,
                                    schedule.workout.id
                                )
                            },
                            hasCompletedWorkouts = false
                        )
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
            .height(200.dp)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.workoutschedule),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay content on the banner
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column {
                Text(
                    text = "Workout Schedule",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Plan your fitness journey",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun WorkoutEventCard(
    schedule: Schedule,
    onStartClick: () -> Unit,
    onViewClick: () -> Unit,
    onRescheduleClick: () -> Unit,
    hasCompletedWorkouts: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Color indicator - thin vertical line
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(40.dp)
                    .background(
                        Color(schedule.color),
                        shape = RoundedCornerShape(1.5.dp)
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Content section
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Time at the top
                if (schedule.startTime != null && schedule.endTime != null) {
                    Text(
                        text = "${schedule.startTime!!.format(DateTimeFormatter.ofPattern("h:mm a"))} - ${
                            schedule.endTime!!.format(DateTimeFormatter.ofPattern("h:mm a"))
                        }",
                        fontSize = 11.sp,
                        color = Color(0xFF888888),
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Workout title - bold and larger
                Text(
                    text = schedule.workout.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    lineHeight = 18.sp
                )

                // Description/note - smaller and lighter
                if (schedule.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = schedule.note,
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        lineHeight = 16.sp
                    )
                } else if (schedule.workout.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = schedule.workout.description,
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        lineHeight = 16.sp
                    )
                }
            }

            // Action buttons - smaller and more compact
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.End
            ) {
                // Show "View" if workout has been completed before, otherwise "Start"
                Button(
                    onClick = if (hasCompletedWorkouts) onViewClick else onStartClick,
                    modifier = Modifier
                        .height(28.dp)
                        .width(65.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasCompletedWorkouts) Color(0xFF007AFF) else Color(
                            0xFF34C759
                        ),
                        contentColor = Color.White
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (hasCompletedWorkouts) "View" else "Start",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Button(
                    onClick = onRescheduleClick,
                    modifier = Modifier
                        .height(28.dp)
                        .width(75.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9F0A),
                        contentColor = Color.White
                    ),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Reschedule",
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// TODO: Implement StartWorkoutDialog separately
