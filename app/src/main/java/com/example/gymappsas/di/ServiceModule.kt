package com.example.gymappsas.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.gymappsas.MainActivity
import com.example.gymappsas.R
import com.example.gymappsas.util.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import java.io.IOException

@Module
@InstallIn(ServiceComponent::class)
class ServiceModule {

    @ServiceScoped
    @Provides
    fun provideWorkoutPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent =
        PendingIntent.getActivity(
            context,
            420,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra("fromNotification", true)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        workoutPendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        // Create notification channel (Android 8.0+)
        createNotificationChannel(context)

        // Return the notification builder with basic configuration
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setContentIntent(workoutPendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Workout Progress",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Workout progress notifications"
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        fun createNotificationLayout(
            context: Context,
            exerciseName: String?,
            completedCount: Int,
            goal: Int,
            progress: Int,
            timeText: String,
            stateText: String
        ): RemoteViews {
            val whiteColor = ContextCompat.getColor(context, R.color.white)
            val accentColor = ContextCompat.getColor(context, R.color.notification_accent)

            return RemoteViews(context.packageName, R.layout.notification_workout).apply {
                // Set texts
                setTextViewText(R.id.exercise_title, exerciseName ?: "Workout")
                setTextViewText(R.id.sets_text, "$completedCount/$goal sets")
                setTextViewText(R.id.state_text, stateText)
                setTextViewText(R.id.timer_text, timeText)

                // Set text colors
                setTextColor(R.id.exercise_title, whiteColor)
                setTextColor(R.id.sets_text, whiteColor)
                setTextColor(R.id.state_text, whiteColor)
                setTextColor(R.id.timer_text, accentColor)

                // Progress bar - REMOVE THE PROBLEMATIC LINE
                setProgressBar(R.id.progress_bar, 100, progress, false)
                // REMOVE THIS LINE: setInt(R.id.progress_bar, "setProgressTint", accentColor)
            }
        }


        private fun getBitmapFromAsset(context: Context, filePath: String?): Bitmap? {
            if (filePath.isNullOrEmpty()) return null

            return try {
                context.assets.open(filePath).use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            } catch (e: IOException) {
                null
            }
        }
    }
}