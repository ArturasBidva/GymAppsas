package com.example.gymappsas.ui.screens.workoutschedule

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.util.MockSchedulesData
import com.example.gymappsas.util.displayText
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun WorkoutScheduleCalendar(
    workoutScheduleUiState: WorkoutScheduleUiState,
    calendarState: CalendarState,
    daysOfWeek: List<DayOfWeek>,
    schedules: List<Schedule>,
    onDaySelection: (CalendarDay) -> Unit
) {
    Log.d("amogusas", schedules.toString())
    HorizontalCalendar(
        modifier = Modifier.wrapContentWidth(),
        state = calendarState,
        dayContent = { day ->
            val colors = if (day.position == DayPosition.MonthDate) {
                val dayEpoch = day.date.toEpochDay()
                val schedulesForDay = schedules.filter { it.date.toEpochDay() == dayEpoch }
                val dayColors = schedulesForDay.mapNotNull { schedule ->
                    if (schedule.date.let { LocalDate.ofEpochDay(it.toEpochDay()) } == day.date) {
                        schedule.color.let { (Color(it)) }
                    } else {
                        null
                    }
                }
                dayColors
            } else {
                emptyList()
            }
            Day(
                day = day,
                isSelected = workoutScheduleUiState.selectedCalendarDate == day.date,
                colors = colors,
                onClick = onDaySelection
            )
        },
        monthHeader = {
            MonthHeader(daysOfWeek = daysOfWeek)
        }
    )
}

@Composable
private fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("MonthHeader"),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.displayText(),
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .testTag("MonthDay")
            .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) Color(0xFFFCCA3E) else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) },
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val textColor = when (day.position) {
                DayPosition.MonthDate -> Color.Unspecified
                DayPosition.InDate, DayPosition.OutDate -> Color(0xFFBEBEBE)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    color = textColor,
                    fontSize = 14.sp,
                )
            }

            // Spacer to create space between the number and the box
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (colors.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        colors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .padding(
                                        start = 2.dp,
                                        end = 2.dp,
                                        top = 0.dp
                                    ) // Adjust spacing if needed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DayPrev() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    val daysOfWeek = remember { daysOfWeek() }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val workoutDays = MockSchedulesData.mockSchedules
        .groupBy({ it.date?.toEpochDay() }, { it })

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first(),
    )
    HorizontalCalendar(
        modifier = Modifier.wrapContentWidth(),
        state = state,
        dayContent = { day ->

            val colors = if (day.position == DayPosition.MonthDate) {
                workoutDays[day.date.toEpochDay()].orEmpty()
                    .map {
                        Color(it.color ?: Color.Blue.toArgb())
                    }
            } else {
                // If not a month date, return an empty list for colors
                emptyList()
            }
            Day(
                day = day,
                isSelected = selection == day,
                colors = colors,
            ) { selectedDay ->
                selection = selectedDay
            }
        },
        monthHeader = {
            MonthHeader(daysOfWeek = daysOfWeek)
        }
    )
}