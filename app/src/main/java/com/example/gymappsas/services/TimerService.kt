package com.example.gymappsas.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.gymappsas.MainActivity
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.timer.TimerEvent
import com.example.gymappsas.di.ServiceModule
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

    @Inject
    lateinit var notificationManager: NotificationManager

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var currentExerciseWorkout: ExerciseWorkout? = null
    private var currentSetCount: Int = 0
    private var currentJob: Job? = null
    private var remainingTime: Long = 0L
    private var lastPhase: TimerEvent = TimerEvent.FINISH
    private var currentPhaseTotalTime: Long = 0L

    override fun onCreate() {
        super.onCreate()
        observeTimerEvents()
    }

    private fun observeTimerEvents() {
        lifecycleScope.launch {
            timerEvent.collect { event ->
                currentPhaseTotalTime = when (event) {
                    TimerEvent.EXERCISE -> currentExerciseWorkout?.duration ?: 0L
                    TimerEvent.BREAK -> currentExerciseWorkout?.breakTime ?: 0L
                    else -> currentPhaseTotalTime
                }
                currentExerciseWorkout?.let { updateNotification(it) }
            }
        }
    }

    private fun getPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    it.getParcelableExtra<ExerciseWorkout>("exerciseWorkout")?.let { workout ->
                        currentExerciseWorkout = workout
                        startWorkoutInForeground()
                    } ?: Log.e("TimerService", "ExerciseWorkout data is null.")
                }

                ACTION_STOP_SERVICE -> stopService()

                ACTION_UPDATE_EXERCISE_DATA -> {
                    it.getParcelableExtra<ExerciseWorkout>("exerciseWorkout")?.let { workout ->
                        currentExerciseWorkout = workout
                        currentSetCount = it.getIntExtra("currentSet", -1)
                        currentExerciseWorkout?.completedCount = currentSetCount
                        updateNotification(workout)
                    } ?: Log.e("TimerService", "Updated ExerciseWorkout data is null.")
                }

                ACTION_WORKOUT_FINISHED -> resetExerciseData()
                ACTION_PAUSE_SERVICE -> pauseTimer()
                ACTION_RESUME_SERVICE -> resumeTimer()
                ACTION_FORWARD_SERVICE -> forwardTimer()
                ACTION_BACKWARD_SERVICE -> backTimer()
                else -> Log.e("TimerService", "Unknown action: ${it.action}")
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
        notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_gym_icon)
            setContentIntent(getPendingIntent())
            priority = NotificationCompat.PRIORITY_MAX
            setOngoing(true)
            setOnlyAlertOnce(true)
            setAutoCancel(false)
            setShowWhen(false)
        }
        updateNotification(currentExerciseWorkout!!)
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Workout timer notifications"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    private fun updateNotification(exerciseWorkout: ExerciseWorkout) {
        val progress = calculateProgress()
        val stateText = when (timerEvent.value) {
            TimerEvent.EXERCISE -> "Exercise Time"
            TimerEvent.BREAK -> "Rest Time"
            TimerEvent.PAUSED -> "Paused"
            else -> "Workout"
        }
        val timeText = TimerUtil.getFormattedTime(timerInMillis.value)

        val notificationLayout = ServiceModule.createNotificationLayout(
            context = this,
            exerciseName = exerciseWorkout.exercise?.name,
            completedCount = exerciseWorkout.completedCount,
            goal = exerciseWorkout.goal,
            progress = progress,
            timeText = timeText,
            stateText = stateText
        )

        notificationBuilder.setCustomContentView(notificationLayout)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun startWorkoutTimer(startingTime: Long = 0L) {
        currentJob?.cancel()
        currentPhaseTotalTime = currentExerciseWorkout!!.duration.toLong()
        timerInMillis.value = currentPhaseTotalTime - startingTime
        timerEvent.value = TimerEvent.EXERCISE

        currentJob = CoroutineScope(Dispatchers.Default).launch {
            runTimer(currentExerciseWorkout!!.duration - startingTime) {
                startRestPhase()
            }
        }
    }

    private fun startRestPhase(startingTime: Long = 0L) {
        currentJob?.cancel()
        currentPhaseTotalTime = currentExerciseWorkout!!.breakTime.toLong()
        timerInMillis.value = currentPhaseTotalTime - startingTime
        timerEvent.value = TimerEvent.BREAK

        currentJob = CoroutineScope(Dispatchers.Default).launch {
            runTimer(currentExerciseWorkout!!.breakTime - startingTime) {
                startWorkoutTimer()
            }
        }
    }

    private fun runTimer(durationMillis: Long, onFinish: () -> Unit) {
        currentJob = lifecycleScope.launch(Dispatchers.Default) {
            var timeLeft = durationMillis

            while (timeLeft > 0 && isActive) {
                timerInMillis.value = timeLeft
                delay(1000L)
                timeLeft -= 1000L
            }

            if (isActive) {
                timerInMillis.value = 0L
                onFinish()
            }
        }
    }

    private fun calculateProgress(): Int {
        if (currentPhaseTotalTime <= 0) return 0
        val progress = ((currentPhaseTotalTime - timerInMillis.value) * 100 / currentPhaseTotalTime).toInt()
        return progress.coerceIn(0, 100)
    }

    private fun stopService() {
        timerInMillis.value = 0L
        timerEvent.value = TimerEvent.FINISH
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun resetExerciseData() {
        timerEvent.value = TimerEvent.FINISH
        currentExerciseWorkout = null
        currentSetCount = 0
        timerInMillis.value = 0L

        // Show completion notification
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_gym_icon)
            .setContentTitle("Workout Completed")
            .setContentText("Great job! You've finished your workout.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(getPendingIntent())
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
        stopSelf()
    }

    private fun pauseTimer() {
        lastPhase = timerEvent.value
        remainingTime = currentPhaseTotalTime - timerInMillis.value
        timerEvent.value = TimerEvent.PAUSED
        currentJob?.cancel()
        currentJob = null
        currentExerciseWorkout?.let { updateNotification(it) }
    }

    private fun resumeTimer() {
        if (currentExerciseWorkout != null) {
            when (lastPhase) {
                TimerEvent.EXERCISE -> startWorkoutTimer(remainingTime)
                TimerEvent.BREAK -> startRestPhase(remainingTime)
                else -> startWorkoutTimer(remainingTime)
            }
            timerEvent.value = TimerEvent.RESUMED
        }
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

    override fun onDestroy() {
        super.onDestroy()
        currentJob?.cancel()
        Log.d("TimerService", "Service destroyed.")
    }
}