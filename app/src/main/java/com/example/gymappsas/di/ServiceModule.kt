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
import com.example.gymappsas.util.GetImagePath
import com.example.gymappsas.util.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import java.io.IOException
import java.io.InputStream

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
                putExtra(
                    "fromNotification",
                    true
                )
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context,
        workoutPendingIntent: PendingIntent
    ): NotificationCompat.Builder {
        val category = "workout.exerciseWorkouts.first().exercise.primaryMuscles.first()" // Use workout category
        val exerciseName = "workout.title" // Use workout title (exercise name)
        val progress = 0 // Set actual progress if available
        val textColorPrimary = ContextCompat.getColor(context, R.color.notification_text_primary)
        val textColorSecondary = ContextCompat.getColor(context, R.color.notification_text_secondary)
        val accentColor = ContextCompat.getColor(context, R.color.notification_accent)
        val imagePath = GetImagePath.getExerciseImagePath(category, exerciseName)
        val bitmap = getBitmapFromAsset(context, imagePath)

        val notificationLayout = RemoteViews(context.packageName, R.layout.notification_workout).apply {

            setTextColor(R.id.exercise_title, textColorPrimary)
            setTextColor(R.id.exercise_details, textColorSecondary)
            setTextColor(R.id.timer_text, accentColor)

            // Progress bar
            setProgressBar(R.id.progress_bar, 100, progress, false)
            setInt(R.id.progress_bar, "setProgressTint", accentColor)
            bitmap?.let {
                setImageViewBitmap(R.id.exercise_icon, it)
            } ?: setImageViewResource(R.id.exercise_icon, R.drawable.avatar__1_)
        }

        // Create notification channel (Android 8.0+)
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Workout Progress",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        // Return the notification builder
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setCustomContentView(notificationLayout)
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


    private fun getBitmapFromAsset(context: Context, filePath: String?): Bitmap? {
        val assetManager = context.assets

        val istr: InputStream
        var bitmap: Bitmap? = null
        try {
            istr = assetManager.open(filePath!!)
            bitmap = BitmapFactory.decodeStream(istr)
        } catch (e: IOException) {
            // handle exception
        }

        return bitmap
    }
}
