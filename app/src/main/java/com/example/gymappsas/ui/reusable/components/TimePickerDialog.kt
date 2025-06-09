package com.example.gymappsas.ui.reusable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onTimeConfirm: (LocalTime) -> Unit,
    workoutTime: LocalTime? = null
) {
    val timePickerState = rememberTimePickerState(
        initialHour = workoutTime?.hour ?: 0,
        initialMinute = workoutTime?.minute ?: 0,
        is24Hour = true
    )
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size = 12.dp)
            )
    ) {
        Content(
            timePickerState = timePickerState,
            onDismiss = onDismiss,
            onTimeConfirm = onTimeConfirm
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    timePickerState: TimePickerState,
    onDismiss: () -> Unit,
    onTimeConfirm: (LocalTime) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = Color.LightGray.copy(alpha = 0.3f))
            .padding(top = 28.dp, bottom = 12.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeInput(state = timePickerState)
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onDismiss) { Text(text = "Dismiss") }
            TextButton(
                onClick = {
                    onTimeConfirm(
                        LocalTime.of(timePickerState.hour, timePickerState.minute)
                    )
                }
            ) {
                Text(text = "Confirm")
            }
        }
    }
}