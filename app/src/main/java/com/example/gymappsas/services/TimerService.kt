package com.example.gymappsas.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.gymappsas.MainActivity
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.timer.TimerEvent
import com.example.gymappsas.util.ACTION_BACKWARD_SERVICE
import com.example.gymappsas.util.ACTION_FORWARD_SERVICE
import com.example.gymappsas.util.ACTION_PAUSE_SERVICE
import com.example.gymappsas.util.ACTION_RESUME_SERVICE
import com.example.gymappsas.util.ACTION_START_SERVICE
import com.example.gymappsas.util.ACTION_STOP_SERVICE
import com.example.gymappsas.util.ACTION_UPDATE_EXERCISE_DATA
import com.example.gymappsas.util.ACTION_WORKOUT_FINISHED
import com.example.gymappsas.util.NOTIFICATION_CHANNEL_ID
import com.example.gymappsas.util.NOTIFICATION_CHANNEL_NAME
import com.example.gymappsas.util.NOTIFICATION_ID
import com.example.gymappsas.util.TimerUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : LifecycleService() {

    companion object {
        val timerEvent = MutableStateFlow<TimerEvent>(TimerEvent.DEFAULT)
        val timerInMillis = MutableStateFlow(0L)
    }

    private var isServiceStopped = false
    private var currentExerciseWorkout: ExerciseWorkout? = null
    private var currentSetCount: Int = 0
    private var currentTimeMillis: Long = 0L
    private var currentJob: Job? = null
    private var remainingTime: Long = 0L
    private var lastPhase: TimerEvent = TimerEvent.FINISH


    @Inject
    lateinit var notificationManager: NotificationManager

    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()
        observeTimer()
    }

    private fun observeTimer() {
        // Instead of observing LiveData, we emit state changes to StateFlow
        lifecycleScope.launch {
            timerInMillis.collect { millis ->
                currentTimeMillis = millis
                currentExerciseWorkout?.let {
                    updateNotificationWithExerciseData(it)
                } ?: Log.w(
                    "TimerService",
                    "currentExerciseWorkout is not initialized. Skipping update."
                )
            }
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    val exerciseWorkout = it.getParcelableExtra<ExerciseWorkout>("exerciseWorkout")
                    if (exerciseWorkout != null) {
                        currentExerciseWorkout = exerciseWorkout
                        startWorkoutInForeground()
                    } else {
                        Log.e("TimerService", "ExerciseWorkout data is null.")
                    }
                }

                ACTION_STOP_SERVICE -> stopService()

                ACTION_UPDATE_EXERCISE_DATA -> {
                    val updatedExerciseWorkout =
                        it.getParcelableExtra<ExerciseWorkout>("exerciseWorkout")
                    val currentSet = it.getIntExtra("currentSet", -1)
                    if (updatedExerciseWorkout != null) {
                        currentExerciseWorkout = updatedExerciseWorkout
                        currentSetCount = currentSet

                        // Update the completed count
                        currentExerciseWorkout?.completedCount = currentSetCount
                        updateNotificationWithExerciseData(updatedExerciseWorkout)
                    } else {
                        Log.e("TimerService", "Updated ExerciseWorkout data is null.")
                    }
                }

                ACTION_WORKOUT_FINISHED -> {
                    resetExerciseData()

                }

                ACTION_PAUSE_SERVICE -> {
                    pauseTimer()
                }

                ACTION_RESUME_SERVICE -> {
                    resumeTimer()
                }

                ACTION_FORWARD_SERVICE -> {
                    forwardTimer()
                }

                ACTION_BACKWARD_SERVICE -> {
                    backTimer()
                }

                else -> {
                    Log.e("TimerService", "Unknown action received: ${it.action}")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startWorkoutInForeground() {
        createNotificationChannel()
        setupInitialNotification()
        startWorkoutTimer()
    }

    private fun setupInitialNotification() {
        val notificationLayout = RemoteViews(packageName, R.layout.notification_workout).apply {
            setTextViewText(R.id.exercise_title, currentExerciseWorkout?.exercise?.name)
            setTextViewText(
                R.id.exercise_details,
                "${currentExerciseWorkout?.completedCount} of ${currentExerciseWorkout?.goal} Sets Completed"
            )
            setTextViewText(R.id.timer_text, TimerUtil.getFormattedTime(0L))
        }

        notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setOnlyAlertOnce(true)
            .setContentIntent(getPendingIntent())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        startForeground(
            NOTIFICATION_ID,
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH).build()
        )
    }

    private fun stopService() {
        isServiceStopped = true
        timerInMillis.value = 0L
        timerEvent.value = TimerEvent.FINISH
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun startWorkoutTimer(startingTime: Long = 0L) {
        Log.d("TimerService", "StartWorkoutTimer is triggered")

        // Cancel any ongoing job before starting a new one
        currentJob?.cancel()

        // Launch the workout timer coroutine if it's not paused
        currentJob = CoroutineScope(Dispatchers.Default).launch {
            Log.d("TimerService", "Workout phase started.")
            timerEvent.value = TimerEvent.EXERCISE  // Set phase to WORKOUT

            // Run the workout timer and transition to rest after it ends
            runTimer(currentExerciseWorkout!!.duration - startingTime) {
                Log.d("TimerService", "Workout phase finished.")
                startRestPhase() // Transition to rest after workout ends
            }
        }
    }


    private fun startRestPhase(startingTime: Long = 0L) {
        // Cancel any ongoing job before starting a new one
        currentJob?.cancel()

        // Launch the rest timer coroutine if it's not paused
        currentJob = CoroutineScope(Dispatchers.Default).launch {
            timerEvent.value = TimerEvent.BREAK // Set phase to REST
            Log.d("TimerService", "Rest phase started.")

            // Run the rest timer and transition back to workout after it ends
            runTimer(currentExerciseWorkout!!.breakTime - startingTime) {
                Log.d("TimerService", "Rest phase finished.")
                startWorkoutTimer() // Transition back to workout after rest ends
            }
        }
    }

    private fun updateNotificationWithExerciseData(
        exerciseWorkout: ExerciseWorkout
    ) {
        val notificationLayout = RemoteViews(packageName, R.layout.notification_workout).apply {
            setTextViewText(R.id.exercise_title, exerciseWorkout.exercise.name)
            setTextViewText(
                R.id.exercise_details,
                "${exerciseWorkout.completedCount} of ${exerciseWorkout.goal} Sets Completed"
            )
            val phaseText = when (timerEvent.value) {
                TimerEvent.EXERCISE -> "Exercise"
                TimerEvent.BREAK -> "Rest"
                else -> ""
            }
            val formattedTime = TimerUtil.getFormattedTime(timerInMillis.value ?: 0L)
            setTextViewText(R.id.timer_text, "$phaseText $formattedTime")

            val elapsedTime = exerciseWorkout.duration - (timerInMillis.value ?: 0L)
            val progress = if (exerciseWorkout.duration > 0) {
                ((elapsedTime * 100) / exerciseWorkout.duration).toInt()
            } else {
                0
            }
            setProgressBar(R.id.progress_bar, 100, progress, false)
        }

        notificationBuilder.setCustomContentView(notificationLayout)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun runTimer(durationMillis: Long, onFinish: () -> Unit) {
        currentJob?.cancel() // Cancel any existing job before starting a new one

        currentJob = lifecycleScope.launch(Dispatchers.Default) {
            var timeLeft = durationMillis

            while (timeLeft > 0 && isActive) {
                timerInMillis.value = timeLeft // Update the timer state
                delay(1000L) // Wait for 1 second
                timeLeft -= 1000L // Decrease the time left
            }

            // Ensure timer properly finishes at 0
            if (isActive) {
                timerInMillis.value = 0L // Set final timer value to 0
                onFinish() // Call onFinish when timer ends
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        isServiceStopped = true
        Log.d("TimerService", "Service destroyed.")
    }


    private fun updateNotificationForFinishedWorkout() {
        stopForeground(STOP_FOREGROUND_REMOVE)

        val notificationLayout = RemoteViews(packageName, R.layout.notification_workout).apply {
            setTextViewText(R.id.exercise_title, "Workout Completed")
            setTextViewText(R.id.exercise_details, "Great job! You've finished your workout.")
            setTextViewText(R.id.timer_text, "") // Clear timer text
            setProgressBar(R.id.progress_bar, 100, 100, false) // Set progress to 100%
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(
                "notification_action",
                "stop_service"
            )
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setCustomContentView(notificationLayout)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)

        Log.d("TimerService", "Workout completed notification displayed.")
    }

    private fun resetExerciseData() {
        isServiceStopped = true
        timerEvent.value = TimerEvent.FINISH
        currentExerciseWorkout = null
        currentSetCount = 0
        timerInMillis.value = 0L
        Log.d("TimerService", "Exercise data reset in TimerService.")
        updateNotificationForFinishedWorkout()
    }


    private fun forwardTimer() {
        currentJob?.cancel()
        timerInMillis.value = 0L
        timerEvent.value = TimerEvent.FORWARD
        startWorkoutTimer()
    }

    private fun backTimer() {
        currentJob?.cancel()
        timerInMillis.value = 0L
        timerEvent.value = TimerEvent.BACKWARD
        startWorkoutTimer()
    }

    private fun pauseTimer() {
        lastPhase = timerEvent.value
        remainingTime = currentExerciseWorkout!!.duration - timerInMillis.value
        timerEvent.value = TimerEvent.PAUSED
        currentJob?.cancel()
        currentJob = null
    }


    private fun resumeTimer() {
        if (currentExerciseWorkout != null) {
            if (currentJob?.isActive == true) {
                Log.d("TimerService", "Timer is already active. Skipping resume.")
                return
            }
            when (lastPhase) {
                TimerEvent.EXERCISE -> {
                    startWorkoutTimer(remainingTime)
                }

                TimerEvent.BREAK -> {
                    startRestPhase(remainingTime)
                }

                else -> {
                    Log.d(
                        "TimerService",
                        "Timer not in EXERCISE or BREAK phase. Current state: $lastPhase"
                    )
                }
            }

            timerEvent.value = TimerEvent.RESUMED
        }
    }
}