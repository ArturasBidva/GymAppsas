package com.example.gymappsas.ui.screens.chooseworkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.util.GetImagePath
import com.example.gymappsas.util.MockWorkoutData
import com.example.gymappsas.util.TimerUtil

@Composable
fun ChooseWorkout(
    viewModel: ChooseWorkoutViewModel,
    onStartClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val workoutUiState by viewModel.uiState.collectAsState()
    val workouts = workoutUiState.workouts
    Content(workouts = workouts, onStartClick = { onStartClick(it) }, onBackClick = onBackClick)
}

@Composable
private fun Content(
    workouts: List<Workout>,
    onStartClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 58.dp)) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(218.dp)
                    .background(Color.LightGray)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.startworkout),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = "Welcome to your workout",
                fontFamily = lexenBold,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(vertical = 20.dp, horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            Text(
                text = "We'll help you set and achieve fitness goals. Let's get started!",
                fontFamily = lexendRegular,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            workouts.forEach { workout ->
                val totalTimeInMillis = workout.exerciseWorkouts.sumOf {
                    (it.duration + it.breakTime) * it.goal
                }
                val formattedTime = TimerUtil.getFormattedTime(totalTimeInMillis)
                WorkoutCard(
                    timeRange = formattedTime,
                    title = workout.title,
                    description = workout.description,
                    exercise = workout.exerciseWorkouts.first().exercise,
                    onStartClick = { onStartClick(it) },
                    workoutId = workout.id
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChooseWorkoutPrev() {
    Content(workouts = MockWorkoutData.mockWorkouts, {}, {})
}

@Composable
fun WorkoutCard(
    timeRange: String,
    title: String,
    description: String,
    workoutId: Long,
    exercise: Exercise,
    onStartClick: (Long) -> Unit
) {
    val maxDescriptionLength = 33
    val truncatedDescription = if (description.length > maxDescriptionLength) {
        description.take(maxDescriptionLength) + "..."
    } else {
        description
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$timeRange min",
                        color = Color(0xFF9CA3AF), // Soft gray color for time range
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = lexendRegular
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = title,
                        color = Color(0xFF111827), // Dark text for title
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = lexenBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = truncatedDescription,
                        color = Color(0xFF6B7280), // Light gray for description
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = lexendRegular
                    )
                }

                // Exercise Image
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(
                            Color(0xFFE5E7EB),
                            shape = RoundedCornerShape(10.dp)
                        ) // Lighter background with soft corners
                ) {
                    AsyncImage(
                        model = GetImagePath.getExerciseImagePath(
                            exerciseName = exercise.name,
                            category = exercise.primaryMuscles.first()
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(10.dp)) // Rounded corners for image
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Start button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF3B82F6)) // Bright blue for Start button
                        .clickable { onStartClick(workoutId) }
                ) {
                    Text(
                        "Start",
                        fontSize = 16.sp,
                        fontFamily = lexenBold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(vertical = 12.dp)
                    )
                }

                // Preview button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9)) // Light gray background for Preview
                        .clickable { }
                        .border(
                            1.dp,
                            Color(0xFFCBD5E1),
                            RoundedCornerShape(12.dp)
                        ) // Subtle border for Preview
                ) {
                    Text(
                        "Preview",
                        fontSize = 16.sp,
                        fontFamily = lexenBold,
                        color = Color(0xFF1F2937), // Dark text for Preview
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}