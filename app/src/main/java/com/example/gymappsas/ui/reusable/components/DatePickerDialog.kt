package com.example.gymappsas.ui.reusable.components

import android.annotation.SuppressLint
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDatePickerDialog(
    onTimeValidation: (Long) -> Boolean,
    onDateSelect : (LocalDate) -> Unit,
    dialogVisibility: (Boolean) -> Unit,
    selectedDateCallback: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
    DatePickerDialog(
        onDismissRequest = {
            val selectedDate = datePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            }
            if (selectedDate != null) {
                onDateSelect(selectedDate)
                selectedDateCallback(selectedDate)
            }
            dialogVisibility(false)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogVisibility(false)
                    val selectedDate = datePickerState.selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    if (selectedDate != null) {
                        onDateSelect(selectedDate)
                        selectedDateCallback(selectedDate)
                    }

                },
                enabled = confirmEnabled.value
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dialogVisibility(false)
                }
            ) {
                Text("Cancel")
            }
        }

    ) {
        DatePicker(state = datePickerState)
    }
}