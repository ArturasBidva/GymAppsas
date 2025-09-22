package com.example.gymappsas.ui.screens.exercisesbycategory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory.Companion.getCategoryEmoji
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.ui.reusable.LoadingCircle
import com.example.gymappsas.ui.screens.workout.WorkoutViewModel

@Composable
fun ExercisesScreen(
    exerciseViewModel: ExerciseViewModel,
    workoutViewModel: WorkoutViewModel,
    navigateToExercisesByCategoryScreen: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit
) {
    val exerciseUiState by exerciseViewModel.uiState.collectAsState()
    val workoutUiState by workoutViewModel.uiState.collectAsState()

    if (exerciseUiState.isLoading || workoutUiState.isLoading) {
        LoadingCircle()
    } else {
        Content(
            exerciseUiState = exerciseUiState,
            navigateToExercisesByCategoryScreen = { navigateToExercisesByCategoryScreen(it) },
            isExpanded = exerciseUiState.isExpanded,
            onExpandedChange = onExpandedChange,
            recentViewedExercises = exerciseUiState.recentViewedExercises,
            onClearRecentlyViewed = { exerciseViewModel.clearRecentlyViewed() }
        )
    }
}

@Composable
private fun Content(
    exerciseUiState: ExerciseUiState,
    navigateToExercisesByCategoryScreen: (String) -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    recentViewedExercises: List<Exercise>,
    onClearRecentlyViewed: () -> Unit
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        ExercisesScreenWithSearchAndLists(
            exercises = exerciseUiState.exercises,
            categories = ExerciseCategory.getAllCategories(),
            popularExercises = emptyList(),
            recentlyViewed = recentViewedExercises,
            onClearRecentlyViewed = { onClearRecentlyViewed() },
            onExerciseClick = {},
            onCategoryClick = { navigateToExercisesByCategoryScreen(it) },
            isExpanded = isExpanded,
            onExpandedChange = onExpandedChange
        )
    }
}


@Composable
fun ExercisesScreenWithSearchAndLists(
    exercises: List<Exercise>,
    categories: List<ExerciseCategory>,
    popularExercises: List<Exercise>,
    recentlyViewed: List<Exercise>,
    onCategoryClick: (String) -> Unit,
    onExerciseClick: (Exercise) -> Unit,
    onClearRecentlyViewed: () -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Search bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "Search exercises",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            )
            IconButton(onClick = { /* TODO: open filter */ }) {
                Icon(
                    Icons.Default.List,
                    contentDescription = "Filter",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Exercise Categories",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.heightIn(max = if (isExpanded) 600.dp else 300.dp),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            val displayedCategories = if (isExpanded) categories else categories.take(4)
            items(displayedCategories) { category ->
                CategoryCard(
                    categoryName = category.name,
                    count = exercises.count { exercise ->
                        exercise.primaryMuscles.any { muscle -> muscle == category.name }
                    },
                    emojiIcon = getCategoryEmoji(category.name),
                    onClick = { onCategoryClick(category.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { onExpandedChange(!isExpanded) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = if (isExpanded) "See Less" else "See All",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
        }

        // Popular Exercises Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Popular Exercises",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = { /* TODO: see all */ }) {
                Text("See All", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Popular Exercises list
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            popularExercises.forEach { exercise ->
                PopularExerciseCard(
                    exercise = exercise,
                    onClick = { onExerciseClick(exercise) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recently Viewed Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recently Viewed",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = { onClearRecentlyViewed() }) {
                Text("Clear", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Recently Viewed List (simple)
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
        ) {
            recentlyViewed.forEachIndexed { index, exercise ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExerciseClick(exercise) }
                        .padding(16.dp)
                ) {
                    Text(
                        text = exercise.name,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = exercise.primaryMuscles.joinToString(", "),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (index < recentlyViewed.lastIndex) {
                    Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}


@Preview
@Composable
private fun ExerciseScreenPreview() {
    Content(
        exerciseUiState = ExerciseUiState(),
        navigateToExercisesByCategoryScreen = {},
        isExpanded = false,
        onExpandedChange = {},
        recentViewedExercises = emptyList(),
        onClearRecentlyViewed = {}
    )
}

@Composable
fun CategoryCard(
    categoryName: String,
    count: Int,
    emojiIcon: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable { }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emojiIcon, fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = categoryName,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$count exercises",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
fun PopularExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .height(96.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(exercise.images),
            contentDescription = exercise.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(96.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp, horizontal = 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = exercise.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = exercise.primaryMuscles.joinToString(", "),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Go to exercise",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
