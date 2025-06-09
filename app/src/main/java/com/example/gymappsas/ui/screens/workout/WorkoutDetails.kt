package com.example.gymappsas.ui.screens.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.notosands
import com.example.gymappsas.ui.screens.exercisesbyselectedcategory.ExerciseItem
import com.example.gymappsas.util.GetImagePath
import com.example.gymappsas.util.MockWorkoutData

@Composable
fun WorkoutDetailsScreen(
    viewModel: WorkoutViewModel,
    navigateToExerciseDetails: (Long) -> Unit,
    onNavigateBackClick: () -> Unit,
    onDeleteWorkoutClick: (Long) -> Unit
) {
    val workoutInfoState = viewModel.uiState.collectAsState()
    workoutInfoState.value.selectedWorkout?.let { workout ->
        Content(
            workout = workout,
            navigateToExerciseDetails = { navigateToExerciseDetails(it) },
            onNavigateBackClick = onNavigateBackClick,
            onDeleteWorkoutClick = {onDeleteWorkoutClick(it)}
        )
    }
}

@Composable
private fun Content(
    workout: Workout,
    navigateToExerciseDetails: (Long) -> Unit,
    onNavigateBackClick: () -> Unit,
    onDeleteWorkoutClick: (Long) -> Unit
) {
    MaterialTheme {
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = 72.dp) // Add padding to avoid overlapping the button
                ) {
                    BannerSection(
                        imageUrl =
                        GetImagePath.getExerciseImagePath(
                            category = workout.exerciseWorkouts.first().exercise.primaryMuscles.first(),
                            exerciseName = workout.exerciseWorkouts.first().exercise.name
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = workout.title,
                        fontFamily = lexenBold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111418),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Text(
                        text = workout.description,
                        fontFamily = notosands,
                        fontSize = 16.sp,
                        color = Color(0xFF111418),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Exercises",
                        fontFamily = lexenBold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111418),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    workout.exerciseWorkouts.forEach { exerciseWorkout ->
                        ExerciseItem(
                            exercise = exerciseWorkout.exercise,
                            onDeleteClick = {},
                            onSelectExerciseClick = { navigateToExerciseDetails(exerciseWorkout.exercise.id) }
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F2F5))
                        .clickable { onDeleteWorkoutClick(workout.id)},
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Delete Workout",
                        fontFamily = lexenBold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFF121417)
                    )
                }
            }
        }
    }
}

@Composable
fun BannerSection(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(218.dp)
            .background(Color.LightGray)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun DialogPreview() {
    Content(
        workout = MockWorkoutData.mockWorkouts[1],
        navigateToExerciseDetails = {},
        onNavigateBackClick = {},
        onDeleteWorkoutClick = {}
    )
}

