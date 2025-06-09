package com.example.gymappsas.ui.reusable.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.schedules.Schedule
import com.example.gymappsas.ui.reusable.quicksandBold
import com.example.gymappsas.ui.reusable.quicksandMedium
import com.example.gymappsas.util.MockSchedulesData
import com.example.gymappsas.util.toFormattedString
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDate

@Composable
fun WorkoutScheduleEvents(
    schedule: Schedule,
    deleteSchedule: (LocalDate, Long) -> Unit,
    setScheduleForEdit: (Schedule) -> Unit,
) {
    val delete = SwipeAction(
        onSwipe = { deleteSchedule(schedule.date, schedule.workout.id) },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        },
        background = Color.Red
    )

    val edit = SwipeAction(
        onSwipe = { setScheduleForEdit(schedule) },
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        },
        background = Color.Green
    )

    SwipeableActionsBox(
        startActions = listOf(edit),
        endActions = listOf(delete)
    ) {
        schedule.let { schedule ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8F8F8))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color((schedule.color)))
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        val startTime = schedule.startTime.toFormattedString()
                        val endTime = schedule.endTime.toFormattedString()
                        val date = schedule.date.toFormattedString()
                        Text(
                            text = "$startTime - $endTime",
                            fontSize = 12.sp,
                            fontFamily = quicksandMedium,
                            color = Color(0xFF8F9BB3)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = date,
                            fontSize = 12.sp,
                            fontFamily = quicksandMedium,
                            color = Color(0xFF8F9BB3)
                        )
                        Image(
                            painterResource(id = R.drawable.baseline_more_horiz_24),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = schedule.workout.title,
                        fontSize = 16.sp,
                        fontFamily = quicksandBold,
                        color = Color(0xFF222B45)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = schedule.workout.description,
                        fontSize = 12.sp,
                        fontFamily = quicksandMedium,
                        color = Color(0xFF8F9BB3)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CalendarEventBoxPreview() {
    WorkoutScheduleEvents(
        schedule = MockSchedulesData.mockSchedules[0],
        deleteSchedule = { date, workoutId ->
            // This is a placeholder implementation for the preview
            println("Deleting schedule for date: $date, workoutId: $workoutId")
        },
        setScheduleForEdit = {}
    )
}