package com.example.gymappsas.util

object TimerUtil {
    fun getFormattedTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000 // Convert milliseconds to seconds
        val minutes = totalSeconds / 60  // Calculate minutes
        val seconds = totalSeconds % 60 // Calculate remaining seconds

        // Ensure that both minutes and seconds are always displayed as two digits
        return String.format("%02d:%02d", minutes, seconds)
    }
}

