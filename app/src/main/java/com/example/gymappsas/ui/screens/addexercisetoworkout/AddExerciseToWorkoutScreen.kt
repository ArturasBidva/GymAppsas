package com.example.gymappsas.ui.screens.addexercisetoworkout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.ui.screens.createworkout.CreateWorkoutViewModel
import com.example.gymappsas.util.GetImagePath
import com.example.gymappsas.util.MockExerciseData


@Composable
fun AddExerciseToWorkout(
    viewModel: AddExerciseToWorkoutViewModel,
    createWorkoutViewModel: CreateWorkoutViewModel,
    onNavigateBack: () -> Unit,
    onSelectedExerciseChange: (Exercise) -> Unit,
    navigateToExerciseDetails: (Long) -> Unit,
    onSaveWorkoutClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val createWorkoutState by createWorkoutViewModel.uiState.collectAsState()
    Content(
        categories = createWorkoutState.selectedCategories,
        exerciseList = state.exercises,
        onNavigateBack = onNavigateBack,
        onSelectedExerciseChange = { onSelectedExerciseChange(it) },
        navigateToExerciseDetails = { navigateToExerciseDetails(it) },
        selectedExercises = state.selectedExercises,
        onSaveWorkoutClick = { onSaveWorkoutClick() },
        isLoading = createWorkoutState.isLoading
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    categories: List<String>,
    exerciseList: List<Exercise>,
    onNavigateBack: () -> Unit,
    onSelectedExerciseChange: (Exercise) -> Unit,
    navigateToExerciseDetails: (Long) -> Unit,
    selectedExercises: List<Exercise>,
    onSaveWorkoutClick: () -> Unit,
    isLoading : Boolean
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Spacer(Modifier.height(20.dp))
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                categories.forEach { category ->
                    stickyHeader {
                        CategoryHeader(category.replaceFirstChar { it.uppercase() })
                    }
                    val exercisesInCategory = exerciseList.filter { exercise ->
                        exercise.primaryMuscles.any { it -> it == category.replaceFirstChar { it.lowercaseChar() } }
                    }
                    exercisesInCategory.forEach { exercise ->
                        item {
                            ListItemas(
                                exercise = exercise,
                                selectedExercises = selectedExercises,
                                onExerciseCheckedChange = { onSelectedExerciseChange(exercise) },
                                navigateToExerciseDetails = { navigateToExerciseDetails(it) })
                        }
                    }
                }

            }
            if (selectedExercises.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0A6BD9))
                        .clickable(enabled = !isLoading) {
                            onSaveWorkoutClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Save Workout",
                        fontFamily = lexenBold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFFF7FAFC)
                    )
                }
                Spacer(Modifier.height(66.dp))
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun ContentPreview() {
    Content(
        categories = ExerciseCategory.getAllCategories().map { it.name },
        exerciseList = MockExerciseData.mockExercises,
        onNavigateBack = {},
        onSelectedExerciseChange = {},
        navigateToExerciseDetails = {},
        selectedExercises = listOf(),
        onSaveWorkoutClick = {},
        isLoading = true
    )
}

@Composable
fun ListItemas(
    exercise: Exercise,
    selectedExercises: List<Exercise>,
    onExerciseCheckedChange: (Boolean) -> Unit,
    navigateToExerciseDetails: (Long) -> Unit
) {
    val isChecked = selectedExercises.contains(exercise)

    Column {
        ListItem(
            headlineContent = {
                Text(
                    modifier = Modifier.clickable { navigateToExerciseDetails(exercise.id) },
                    text = exercise.name, fontFamily = lexendRegular
                )
            },
            leadingContent = {
                val imageUrl = GetImagePath.getExerciseImagePath(
                    category = exercise.primaryMuscles.first(),
                    exerciseName = exercise.name,
                    imageIndex = 0
                )

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            },
            trailingContent = {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onExerciseCheckedChange(it) }
                )
            }
        )
        HorizontalDivider()
    }
}

@Composable
private fun CategoryHeader(
    headerText: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = headerText, fontFamily = lexenBold,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategorizedLazyColumn(
    category: String,
    exercises: List<Exercise>,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        stickyHeader {
            CategoryHeader(category)
        }
        items(exercises) {
            ListItemas(exercise = it, listOf(), {}, {})
        }
    }
}

@Preview
@Composable
private fun StickyListPreview() {
    CategorizedLazyColumn(category = "Kategorija", exercises = MockExerciseData.mockExercises)
}