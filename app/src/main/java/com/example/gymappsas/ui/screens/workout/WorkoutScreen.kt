package com.example.gymappsas.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.data.db.models.workouts.WorkoutCategory
import com.example.gymappsas.data.db.models.workouts.WorkoutVariant
import com.example.gymappsas.ui.reusable.LoadingCircle
import com.example.gymappsas.util.MockExerciseWorkoutData
import com.example.gymappsas.util.MockProfileData
import com.example.gymappsas.util.TimerUtil

@Composable
fun WorkoutScreen(
    workoutViewModel: WorkoutViewModel = hiltViewModel(),
    onCreateWorkoutClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onStartClick: (Long) -> Unit,
    onViewClick: (Long) -> Unit,
    onStarClick: (Long) -> Unit,
    onMarkedStarClick: (Long) -> Unit
) {
    val workoutUiState by workoutViewModel.uiState.collectAsState()

    if (workoutUiState.isLoading) {
        LoadingCircle()
    } else {
        MyWorkoutsScreen(
            onStartClick = onStartClick,
            onViewClick = onViewClick,
            onCreateWorkoutClick = onCreateWorkoutClick,
            workoutUiState = workoutUiState,
            onSearchChange = onSearchChange,
            onStarClick = { onStarClick(it) },
            onMarkedStarClick = { onMarkedStarClick(it) },
            clearUIText = { workoutViewModel.clearUiText() }
        )
    }
}

@Composable
private fun MyWorkoutsScreen(
    onStartClick: (Long) -> Unit,
    onStarClick: (Long) -> Unit,
    onViewClick: (Long) -> Unit,
    onCreateWorkoutClick: () -> Unit,
    workoutUiState: WorkoutUiState,
    onSearchChange: (String) -> Unit,
    onMarkedStarClick: (Long) -> Unit,
    clearUIText: () -> Unit,
) {
    // Use workouts directly from UI state
    val workouts = workoutUiState.workouts
    val recentlyUsedWorkouts = workouts.filter { it.hasRecentActivity }

    var tabIndex by remember { mutableIntStateOf(0) }
    val categories = listOf("All") + WorkoutCategory.entries.map { it.displayName }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(workoutUiState.uiText) {
        workoutUiState.uiText?.let { uiText ->
            snackbarHostState.showSnackbar(
                message = uiText.asString(context = context),
                duration = SnackbarDuration.Short
            )
            clearUIText()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateWorkoutClick,
                containerColor = Color(0xFF2563EB),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add workout",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        containerColor = Color(0xFFF9FAFB)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,
                contentColor = Color(0xFF2563EB)
            ) {
                Tab(
                    selected = tabIndex == 0,
                    onClick = { tabIndex = 0 },
                    text = {
                        Text(
                            "Your Workouts",
                            color = if (tabIndex == 0) Color(0xFF2563EB) else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = tabIndex == 1,
                    onClick = { tabIndex = 1 },
                    text = {
                        Text(
                            "Workout History",
                            color = if (tabIndex == 1) Color(0xFF2563EB) else Color.Gray
                        )
                    }
                )
            }

            when (tabIndex) {
                0 -> YourWorkoutsTab(
                    workouts = workouts,
                    onViewClick = onViewClick,
                    categories = categories,
                    onStarClick = { id -> onStarClick(id) },
                    onMarkedStarClick = { id -> onMarkedStarClick(id) }
                )

                1 -> WorkoutHistoryTab(
                    recentlyUsedWorkouts = recentlyUsedWorkouts,
                    onStartClick = onStartClick,
                    categories = categories
                )
            }
        }
    }
}

@Composable
private fun YourWorkoutsTab(
    workouts: List<Workout>,
    onViewClick: (Long) -> Unit,
    categories: List<String>,
    onStarClick: (Long) -> Unit,
    onMarkedStarClick: (Long) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2563EB),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Workouts list
        val filteredWorkouts = if (selectedCategory == "All") {
            workouts
        } else {
            workouts.filter {
                it.category?.displayName == selectedCategory
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredWorkouts) { workout ->
                FigmaWorkoutCard(
                    workout = workout,
                    onViewDetailsClick = { onViewClick(it) },
                    onAddToFavouriteClick = { onStarClick(it) },
                    onRemoveFromFavouriteClick = { onMarkedStarClick(it) }
                )
            }

            if (filteredWorkouts.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (selectedCategory == "All") "No workouts found" else "No ${selectedCategory.lowercase()} workouts found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF991B1B)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Create your first workout by tapping the + button",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF7F1D1D),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutHistoryTab(
    recentlyUsedWorkouts: List<Workout>,
    onStartClick: (Long) -> Unit,
    categories: List<String>
) {
    var selectedCategory by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2563EB),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        val filteredHistory = if (selectedCategory == "All") {
            recentlyUsedWorkouts
        } else {
            recentlyUsedWorkouts.filter {
                it.category?.displayName == selectedCategory
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredHistory) { workout ->
                RecentWorkoutCard(
                    workout = workout,
                    onRepeatLastClick = { lastVariant ->
                        onStartClick(lastVariant.id)
                    }
                )
            }

            if (filteredHistory.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No workout history found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF991B1B)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Start working out to see your history here",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF7F1D1D),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FigmaWorkoutCard(
    workout: Workout,
    onViewDetailsClick: (Long) -> Unit,
    onAddToFavouriteClick: (Long) -> Unit,
    onRemoveFromFavouriteClick: (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = workout.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF000000)
                        ),
                        color = Color(0xFF000000)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (workout.isFavorite) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFEAB308),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    onRemoveFromFavouriteClick(workout.id)
                                }
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.star_20dp_e8eaed_fill0_wght400_grad0_opsz20),
                            contentDescription = null,
                            tint = Color(0xFF9CA3AF),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    onAddToFavouriteClick(workout.id)
                                }
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${workout.workoutTime} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }


            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                workout.category?.displayName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
                workout.lastUsedVariant?.let { lastUsedVariant ->
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Last done: ${lastUsedVariant.getDaysAgo()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            val exercises = workout.exerciseWorkouts
            val maxVisible = 3
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxLines = 2
            ) {

                exercises.take(maxVisible).forEach { exerciseWorkout ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = exerciseWorkout.exercise.name,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            color = Color(0xFF000000)
                        )
                    }
                }
                if (exercises.size > maxVisible) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = Color(0xFFF3F4F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${exercises.size - maxVisible} more",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp
                            ),
                            color = Color(0xFF000000)
                        )
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable { onViewDetailsClick(workout.id) }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2563EB)
                        )
                    )
                    Icon(
                        painter = painterResource(R.drawable.continue_icon),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF2563EB)
                    )
                }
            }
        }
    }
}


@Composable
fun RecentWorkoutCard(
    workout: Workout,
    onRepeatLastClick: (WorkoutVariant) -> Unit
) {
    val lastUsedVariant = workout.lastUsedVariant

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workout.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF000000)
                    )

                    lastUsedVariant?.let { variant ->
                        Text(
                            text = "Last used: ${variant.getDaysAgo()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6B7280)
                        )

                        Text(
                            text = "${variant.name} • ${variant.estimatedDuration} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                // Repeat Last button
                lastUsedVariant?.let { variant ->
                    Button(
                        onClick = { onRepeatLastClick(variant) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Repeat Last",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun MyWorkoutsScreenPreview() {
    val mockWorkouts = listOf(
        Workout(
            id = 1,
            title = "Push Day Workout",
            description = "Chest, shoulders, and triceps",
            category = WorkoutCategory.STRENGTH,
            exerciseWorkouts = MockExerciseWorkoutData.mockExerciseWorkouts,
            variants = listOf(
                WorkoutVariant(
                    id = 1,
                    name = "Heavy Push",
                    trainingMethod = "Pyramid",
                    restTimeSeconds = 90,
                    lastUsedAt = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000),
                    createdAt = TimerUtil.getFormattedTime(System.currentTimeMillis()),
                    estimatedDuration = 45
                ),
                WorkoutVariant(
                    id = 2,
                    name = "Light Volume",
                    trainingMethod = "Constant",
                    restTimeSeconds = 60,
                    lastUsedAt = null,
                    createdAt = TimerUtil.getFormattedTime(System.currentTimeMillis()),
                    estimatedDuration = 35
                )
            )
        ),
        Workout(
            id = 2,
            title = "Pull Day Workout",
            description = "Back, biceps, and rear delts",
            category = WorkoutCategory.STRENGTH,
            exerciseWorkouts = MockExerciseWorkoutData.mockExerciseWorkouts,
            variants = listOf(
                WorkoutVariant(
                    id = 3,
                    name = "Standard Pull",
                    trainingMethod = "Constant",
                    restTimeSeconds = 75,
                    lastUsedAt = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000),
                    createdAt = TimerUtil.getFormattedTime(System.currentTimeMillis()),
                    estimatedDuration = 40
                )
            )
        )
    )

    val mockWorkoutUiState = WorkoutUiState(
        isLoading = false,
        workouts = mockWorkouts,
        profile = MockProfileData.mockProfile
    )

    MyWorkoutsScreen(
        onSearchChange = {},
        onStartClick = {},
        onViewClick = {},
        onCreateWorkoutClick = {},
        workoutUiState = mockWorkoutUiState,
        onStarClick = {},
        onMarkedStarClick = {},
        clearUIText = {}
    )
}