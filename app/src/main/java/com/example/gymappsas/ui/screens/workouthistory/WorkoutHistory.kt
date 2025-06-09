package com.example.gymappsas.ui.screens.workouthistory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gymappsas.data.db.models.completedworkout.CompletedWorkout
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout

@Composable
fun WorkoutHistory(
    workoutHistoryViewModel: WorkoutHistoryViewModel,
    onNavigateClick: () -> Unit,
    onExerciseClick: (ExerciseWorkout) -> Unit,
    onBackClick: () -> Unit
) {
    val workoutHistoryState by workoutHistoryViewModel.uiState.collectAsState()
    Content(
        workoutHistoryUiState = workoutHistoryState,
        onExerciseClick = onExerciseClick,
        onNavigateClick = onNavigateClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun Content(
    workoutHistoryUiState: WorkoutHistoryUiState,
    onExerciseClick: (ExerciseWorkout) -> Unit,
    onNavigateClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val listState = rememberLazyListState()
    if (workoutHistoryUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (workoutHistoryUiState.workoutHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No workout history available. Tap to start workout.",
                    Modifier.clickable { onNavigateClick() })
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Ensure the LazyColumn takes up the remaining space
                        state = listState
                    ) {

                        }
                    }
                }
            }
        }
    }


@Composable
fun WorkoutHistoryItemView(
    completedWorkout: CompletedWorkout,
    onExerciseClick: (ExerciseWorkout) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "Date: ${completedWorkout.completedDate}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Workout: ${completedWorkout.workout.title}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        completedWorkout.workout.exerciseWorkouts.forEach { exercise ->
            ExerciseWorkoutItemView(
                exerciseWorkout = exercise,
                onClick = { onExerciseClick(exercise) }
            )
        }
    }
}

@Composable
fun ExerciseWorkoutItemView(
    exerciseWorkout: ExerciseWorkout,
    onClick: () -> Unit
) {

}

@Preview
@Composable
private fun WorkoutHistoryPreview() {
    Content(workoutHistoryUiState = WorkoutHistoryUiState(), onExerciseClick = {}, {}) {

    }
}