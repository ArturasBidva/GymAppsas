package com.example.gymappsas.ui.screens.workoutschedule

sealed class TimeSelectionDialogType {
    data object StartTime: TimeSelectionDialogType()
    data object EndTime: TimeSelectionDialogType()
}