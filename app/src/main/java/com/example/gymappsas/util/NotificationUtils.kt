package com.example.gymappsas.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

const val WORKOUT_NOTIFICATION_CHANNEL_ID = "workout_channel"
fun createWorkoutNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            WORKOUT_NOTIFICATION_CHANNEL_ID,
            "Workout Session",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Tracks current workout progress"
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}