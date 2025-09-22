package com.example.gymappsas.ui.screens.ongoingworkout

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.gymappsas.R
import com.example.gymappsas.RequestNotificationPermissionIfNeeded
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.timer.TimerEvent
import com.example.gymappsas.services.TimerService
import com.example.gymappsas.ui.AppTheme
import com.example.gymappsas.util.ACTION_BACKWARD_SERVICE
import com.example.gymappsas.util.ACTION_FORWARD_SERVICE
import com.example.gymappsas.util.ACTION_PAUSE_SERVICE
import com.example.gymappsas.util.ACTION_RESUME_SERVICE
import com.example.gymappsas.util.ACTION_START_SERVICE
import com.example.gymappsas.util.ACTION_UPDATE_EXERCISE_DATA
import com.example.gymappsas.util.ACTION_WORKOUT_FINISHED
import com.example.gymappsas.util.GetImagePath.getExerciseImagePath
import com.example.gymappsas.util.TimerUtil

@Composable
fun OnGoingWorkoutScreen(
    onGoingWorkoutViewModel: OnGoingWorkoutViewModel,
    onExpand : (Boolean) -> Unit
) {
    RequestNotificationPermissionIfNeeded()
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
        WorkoutSessionScreen(
            uiState = uiState,
            elapsedTime = elapsedTime,
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
            },
            onClose = { },
            timerEvent = timerEvent,
            currentExercise = uiState.currentExercise ?: ExerciseWorkout(),
            onExpand = { onExpand(it) }
        )
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .navigationBarsPadding()
    ) {
        // Central Play/Pause/Resume Button
        IconButton(
            onClick = {
                when {
                    isTimerRunning -> onPauseClick()
                    isPaused -> onResumeClick()
                    else -> onPlayClick()
                }
            },
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .background(Color(0xFF3B82F6), shape = CircleShape)
                .padding(12.dp)
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
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // Previous Button, visible only when timer running
        AnimatedVisibility(
            visible = isTimerRunning,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            IconButton(
                onClick = onPreviousClick,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(56.dp)
                    .background(Color(0xFFF3F4F6), shape = CircleShape)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.skip_previous),
                    contentDescription = "Previous Set",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Next Button, visible only when timer running
        AnimatedVisibility(
            visible = isTimerRunning,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            IconButton(
                onClick = onNextClick,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(56.dp)
                    .background(Color(0xFFF3F4F6), shape = CircleShape)
                    .padding(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.skip_next),
                    contentDescription = "Next Set",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun ContentPreview() {
    AppTheme(dynamicColor = false) {
        WorkoutSessionScreen(
            uiState = OnGoingWorkoutUIState(),
            elapsedTime = 200,
            onPreviousClick = {},
            onPauseClick = {},
            onResumeClick = {},
            onNextClick = {},
            onPlayClick = {},
            onClose = {},
            timerEvent = TimerEvent.PAUSED,
            currentExercise = ExerciseWorkout(),
            onExpand = {}
        )
    }
}

@Composable
fun WorkoutSessionScreen(
    uiState: OnGoingWorkoutUIState,
    elapsedTime: Long,
    onPlayClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    timerEvent: TimerEvent,
    currentExercise: ExerciseWorkout,
    onExpand: (Boolean) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFC))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    currentExercise.exercise.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Tag(currentExercise.exercise.level, Color(0xFFEFF6FF), Color(0xFF3B82F6))
                    Tag(
                        currentExercise.exercise.primaryMuscles.first(),
                        Color(0xFFF3F4F6),
                        Color.Black
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            val imageUrl = getExerciseImagePath(
                category = currentExercise.exercise.primaryMuscles.first(),
                exerciseName = currentExercise.exercise.name,
            )
            AsyncImage(
                model = imageUrl,
                contentDescription = "Exercise image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Set ${uiState.currentSet} of ${uiState.totalSets}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "12 reps",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                LinearProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = Color(0xFF3B82F6),
                    trackColor = Color.White.copy(alpha = 0.3f),
                )
            }
        }

        // Timer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                TimerUtil.getFormattedTime(elapsedTime),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text("Rest Timer", color = Color.Gray)
        }

        WorkoutControls(
            isTimerRunning = uiState.isTimerRunning || timerEvent == TimerEvent.EXERCISE || timerEvent == TimerEvent.FORWARD || timerEvent == TimerEvent.BACKWARD,
            onPlayClick = onPlayClick,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onPauseClick = onPauseClick,
            isPaused = uiState.isPaused,
            onResumeClick = onResumeClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Form Tips
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            InstructionsSection(instructions = currentExercise.exercise.instructions, onExpand = { onExpand(it) })
        }

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onClose,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Gray
                )
            ) {
                Text("End Workout", color = Color.Gray)
            }
            Button(
                onClick = { /* Complete Set */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Complete Set", color = Color.White)
            }
        }
    }
}

@Composable
private fun Tag(text: String, background: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, fontSize = 12.sp, color = textColor)
    }
}

@Composable
fun InstructionsSection(instructions: List<String>, onExpand: (Boolean) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onExpand(expanded)}
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = Color(0xFF3B82F6),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(24.dp)
            )
            Text(
                "Instructions",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val visibleInstructions = if (expanded) instructions else instructions.take(2)

        visibleInstructions.forEach { line ->
            Row(
                modifier = Modifier
                    .padding(start = 32.dp, bottom = 4.dp) // Aligns with the text
            ) {
                Text("•", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    line,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (instructions.size > 2) {
            Text(
                text = if (expanded) "Show less" else "Show more",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3B82F6),
                modifier = Modifier
                    .padding(start = 32.dp, top = 8.dp) // Match alignment
                    .clickable { expanded = !expanded }
            )
        }
    }
}