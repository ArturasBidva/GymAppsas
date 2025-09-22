package com.example.gymappsas.ui.screens.workouthistory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymappsas.data.db.models.completedworkout.CompletedWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.db.models.workouts.WorkoutCategory
import com.example.gymappsas.data.db.models.workouts.WorkoutVariant
import com.example.gymappsas.util.MockExerciseWorkoutData
import com.example.gymappsas.util.TimerUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutVariantsScreen(
    workoutId: Long,
    onBackClick: () -> Unit,
    onStartNewWorkout: () -> Unit,
    onStartVariant: (WorkoutVariant) -> Unit = {},
    viewModel: WorkoutHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(workoutId) {
        viewModel.loadWorkoutHistory(workoutId)
    }

    WorkoutHistoryScreenContent(
        workout = uiState.workout,
        completedWorkouts = uiState.completedWorkouts,
        workoutVariants = uiState.workoutVariants,
        onBackClick = onBackClick,
        onStartNewWorkout = onStartNewWorkout,
        onStartVariant = onStartVariant
    )
}

@Composable
fun WorkoutVariantCard(
    workoutVariant: WorkoutVariant,
    onStartVariant: (WorkoutVariant) -> Unit = {},
    onViewDetails: (WorkoutVariant) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header row with title and star
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = workoutVariant.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937),
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Duration with clock icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = com.example.gymappsas.R.drawable.illustration_1),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFF9CA3AF)
                        )
                        Text(
                            text = "${workoutVariant.estimatedDuration} min",
                            fontSize = 10.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    // Star icon
                    IconButton(
                        onClick = { /* Toggle favorite */ },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = if (workoutVariant.isFavourite) Icons.Default.Star else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (workoutVariant.isFavourite) Color(0xFFFBBF24) else Color(
                                0xFF9CA3AF
                            ),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Training method and rest time badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Training method badge with different colors
                val (bgColor, textColor) = when (workoutVariant.trainingMethod.uppercase()) {
                    "CONSTANT" -> Color(0xFFF0FDF4) to Color(0xFF16A34A)
                    "REVERSE_PYRAMID", "REVERSE PYRAMID" -> Color(0xFFFFF7ED) to Color(0xFFEA580C)
                    "PYRAMID" -> Color(0xFFEFF6FF) to Color(0xFF2563EB)
                    "DROPSET" -> Color(0xFFFDF4FF) to Color(0xFFA855F7)
                    "FORCE" -> Color(0xFFFEF3F2) to Color(0xFFDC2626)
                    else -> Color(0xFFF9FAFB) to Color(0xFF6B7280)
                }

                Box(
                    modifier = Modifier
                        .background(bgColor, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = workoutVariant.trainingMethod.replace("_", " "),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )
                }

                // Rest time badge
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF9FAFB), CircleShape)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = com.example.gymappsas.R.drawable.illustration_1),
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = Color(0xFF9CA3AF)
                        )
                        Text(
                            text = "${workoutVariant.restTimeSeconds}s rest",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4B5563)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Info row with icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Strength category
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = com.example.gymappsas.R.drawable.illustration_1),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF9CA3AF)
                    )
                    Text(
                        text = "Strength",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                // Days ago
                workoutVariant.getDaysAgo()?.let { daysAgo ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = com.example.gymappsas.R.drawable.illustration_1),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFF9CA3AF)
                        )
                        Text(
                            text = daysAgo,
                            fontSize = 10.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                // Exercise count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = com.example.gymappsas.R.drawable.illustration_1),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF9CA3AF)
                    )
                    Text(
                        text = "${workoutVariant.exercises.size} exercises",
                        fontSize = 10.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description text
            Text(
                text = workoutVariant.description ?: "",
                fontSize = 10.sp,
                color = Color(0xFF9CA3AF),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // View Details button
            TextButton(
                onClick = { onViewDetails(workoutVariant) },
                modifier = Modifier.align(Alignment.Start),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF2563EB)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "View Details",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(id = com.example.gymappsas.R.drawable.illustration_1),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF2563EB)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreenContent(
    workout: Workout?,
    completedWorkouts: List<CompletedWorkout>,
    workoutVariants: List<WorkoutVariant>,
    onBackClick: () -> Unit,
    onStartNewWorkout: () -> Unit,
    onStartVariant: (WorkoutVariant) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = workout?.title ?: "Leg Day",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.Black, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF9FAFB)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onStartNewWorkout,
                containerColor = Color(0xFF2563EB),
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = com.example.gymappsas.R.drawable.create_icon),
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = Color(0xFFF9FAFB)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (workoutVariants.isEmpty()) {
                item {
                    EmptyVariantsCard(onStartNewWorkout)
                }
            } else {
                items(workoutVariants) { workoutVariant ->
                    WorkoutVariantCard(
                        workoutVariant = workoutVariant,
                        onStartVariant = onStartVariant,
                        onViewDetails = { /* Handle view details */ }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
            }
        }
    }
}

@Composable
fun EmptyVariantsCard(onStartNewWorkout: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFF3F4F6), RoundedCornerShape(40.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = com.example.gymappsas.R.drawable.create_icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF9CA3AF)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "No workout variants yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create your first workout variant to get started",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStartNewWorkout,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = com.example.gymappsas.R.drawable.create_icon),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create Variant",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutVariantCardPreview() {
    val mockVariant1 = WorkoutVariant(
        id = 1L,
        name = "Standard",
        trainingMethod = "CONSTANT",
        restTimeSeconds = 120,
        lastUsedAt = System.currentTimeMillis() - (4 * 24 * 60 * 60 * 1000), // 4 days ago
        createdAt = TimerUtil.getFormattedTime(System.currentTimeMillis()),
        estimatedDuration = 50,
        isFavourite = true,
        exercises = MockExerciseWorkoutData.mockExerciseWorkouts
    )

    val mockVariant2 = WorkoutVariant(
        id = 2L,
        name = "Light Recovery",
        trainingMethod = "REVERSE_PYRAMID",
        restTimeSeconds = 60,
        lastUsedAt = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000), // 1 week ago
        createdAt = TimerUtil.getFormattedTime(System.currentTimeMillis()),
        estimatedDuration = 35,
        isFavourite = false
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WorkoutVariantCard(mockVariant1)
        WorkoutVariantCard(mockVariant2)
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutHistoryScreenEmptyPreview() {
    WorkoutHistoryScreenContent(
        workout = Workout(id = 1L, title = "Leg Day", category = WorkoutCategory.STRENGTH),
        completedWorkouts = emptyList(),
        workoutVariants = emptyList(),
        onBackClick = {},
        onStartNewWorkout = {},
        onStartVariant = {}
    )
}