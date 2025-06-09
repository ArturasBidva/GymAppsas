package com.example.gymappsas.ui.screens.ongoingworkout

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.timer.TimerEvent
import com.example.gymappsas.services.TimerService
import com.example.gymappsas.util.ACTION_BACKWARD_SERVICE
import com.example.gymappsas.util.ACTION_FORWARD_SERVICE
import com.example.gymappsas.util.ACTION_PAUSE_SERVICE
import com.example.gymappsas.util.ACTION_RESUME_SERVICE
import com.example.gymappsas.util.ACTION_START_SERVICE
import com.example.gymappsas.util.ACTION_UPDATE_EXERCISE_DATA
import com.example.gymappsas.util.ACTION_WORKOUT_FINISHED
import com.example.gymappsas.util.GetImagePath.getExerciseImagePath

@Composable
fun OnGoingWorkout(
    onGoingWorkoutViewModel: OnGoingWorkoutViewModel
) {
    val elapsedTime by onGoingWorkoutViewModel.timerInMillis.observeAsState(0L)
    val timerEvent by onGoingWorkoutViewModel.timerEvent.observeAsState(TimerEvent.EXERCISE)
    val uiState by onGoingWorkoutViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Listen for when the current set or workout state updates
    LaunchedEffect(uiState) {
        Log.d("OnGoingWorkoutScreen", "UI State Observed: $uiState")
    }

    // Trigger services to update exercise data when state changes
    LaunchedEffect(uiState.currentSet) {
        val currentExercise = uiState.currentExercise
        val currentSet = uiState.currentSet
        if (currentExercise != null) {
            val intent = Intent(context, TimerService::class.java).apply {
                action = ACTION_UPDATE_EXERCISE_DATA
                putExtra("exerciseWorkout", currentExercise)
                putExtra("currentSet", currentSet)
            }
            context.startService(intent)
            Log.d(
                "OnGoingWorkout",
                "Intent sent for Exercise: ${currentExercise.exercise.name}, Set: $currentSet"
            )
        } else {
            Log.e("OnGoingWorkout", "currentExercise or currentSet is null.")
        }
    }

    // When the workout is completed, notify the service
    LaunchedEffect(uiState.workoutCompleted) {
        if (uiState.workoutCompleted) {
            val intent = Intent(context, TimerService::class.java).apply {
                action = ACTION_WORKOUT_FINISHED
            }
            context.startService(intent)
        }
    }

    // Display the current workout and timer controls
    uiState.onGoingWorkout?.let {
        OnGoingWorkoutScreen(
            uiState = uiState,
            elapsedTime = elapsedTime,
            timerEvent = timerEvent,
            onPlayClick = {
                val intent = Intent(context, TimerService::class.java).apply {
                    action = ACTION_START_SERVICE
                    putExtra("exerciseWorkout", uiState.currentExercise)
                }
                context.startService(intent)
            },
            onPreviousClick = {
                val intent = Intent(context, TimerService::class.java).apply {
                    action = ACTION_BACKWARD_SERVICE
                }
                context.startService(intent)
            },
            onNextClick = {
                val intent = Intent(context, TimerService::class.java).apply {
                    action = ACTION_FORWARD_SERVICE
                }
                context.startService(intent)
            },
            onPauseClick = {
                val intent = Intent(context, TimerService::class.java).apply {
                    action = ACTION_PAUSE_SERVICE
                }
                context.startService(intent)
            },
            onResumeClick = {
                val intent = Intent(context, TimerService::class.java).apply {
                    action = ACTION_RESUME_SERVICE
                }
                context.startService(intent)
            }
        )
    }
}

@Composable
fun OnGoingWorkoutScreen(
    uiState: OnGoingWorkoutUIState,
    elapsedTime: Long,
    timerEvent: TimerEvent,
    onPlayClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                WorkoutControls(
                    isTimerRunning = uiState.isTimerRunning || timerEvent == TimerEvent.EXERCISE || timerEvent == TimerEvent.FORWARD || timerEvent == TimerEvent.BACKWARD,
                    onPlayClick = onPlayClick,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    onPauseClick = onPauseClick,
                    isPaused = uiState.isPaused,
                    onResumeClick = onResumeClick
                )
            }
        ) { innerPadding ->
            Content(
                modifier = Modifier.padding(innerPadding),
                uiState = uiState,
                elapsedTime = elapsedTime
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    uiState: OnGoingWorkoutUIState,
    elapsedTime: Long
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = uiState.currentExercise?.exercise?.name ?: "Select Exercise",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        TimerCard(elapsedTime = elapsedTime)

        Spacer(modifier = Modifier.height(24.dp))

        ExercisePreview(uiState)

        Spacer(modifier = Modifier.height(24.dp))

        WorkoutProgress(
            currentSet = uiState.currentSet,
            totalSets = uiState.totalSets,
            progress = uiState.progress
        )
    }
}

@Composable
private fun TimerCard(
    elapsedTime: Long,
    modifier: Modifier = Modifier
) {
    val minutes = (elapsedTime / 60000).toString().padStart(2, '0')
    val seconds = ((elapsedTime % 60000) / 1000).toString().padStart(2, '0')

    Card(modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimeUnit(value = minutes, label = "MINUTES")
                Text(":", style = MaterialTheme.typography.displayMedium)
                TimeUnit(value = seconds, label = "SECONDS")
            }
        }
    }
}

@Composable
private fun WorkoutControls(
    isTimerRunning: Boolean,
    onPlayClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPauseClick: () -> Unit,
    isPaused: Boolean,
    onResumeClick: () -> Unit
) {
    Surface(tonalElevation = 8.dp, color = MaterialTheme.colorScheme.surface) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .navigationBarsPadding()
        ) {
            FloatingActionButton(
                onClick = {
                    if (isTimerRunning) {
                        onPauseClick()
                    } else {
                        if (isPaused) {
                            onResumeClick()
                        } else {
                            onPlayClick()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(56.dp),
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    painter = painterResource(
                        id = when {
                            isTimerRunning -> R.drawable.pause
                            isPaused -> R.drawable.play_arrow
                            else -> R.drawable.play_arrow
                        }
                    ),
                    contentDescription = when {
                        isTimerRunning -> "Pause"
                        isPaused -> "Resume"
                        else -> "Start"
                    },
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            AnimatedVisibility(
                visible = isTimerRunning,
                enter = slideInHorizontally { -it },
                exit = slideOutHorizontally { -it },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                IconButton(
                    onClick = onPreviousClick,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.skip_previous),
                        contentDescription = "Previous Set"
                    )
                }
            }

            AnimatedVisibility(
                visible = isTimerRunning,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                IconButton(
                    onClick = onNextClick,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.skip_next),
                        contentDescription = "Next Set"
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeUnit(
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(100.dp, 60.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WorkoutProgress(
    currentSet: Int,
    totalSets: Int,
    progress: Float
) {
    Column {
        Text(
            text = "Workout Progress",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Completed Sets",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$currentSet/$totalSets",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ExercisePreview(uiState: OnGoingWorkoutUIState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Current Exercise Image
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            if (uiState.currentExercise?.exercise != null) {
                AsyncImage(
                    model = getExerciseImagePath(
                        category = uiState.currentExercise.exercise.primaryMuscles[0],
                        exerciseName = uiState.currentExercise.exercise.name
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            } else {
                // Placeholder if no current exercise
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No Exercise Selected")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Up Next Section
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Up Next: ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp)
                )

                if (uiState.nextExercise != null) {
                    AsyncImage(
                        model = getExerciseImagePath(
                            category = uiState.nextExercise.exercise.primaryMuscles[0],
                            exerciseName = uiState.nextExercise.exercise.name
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = uiState.nextExercise.exercise.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        text = "No upcoming exercises",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}