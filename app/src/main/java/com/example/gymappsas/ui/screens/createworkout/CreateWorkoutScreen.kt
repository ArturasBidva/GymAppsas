package com.example.gymappsas.ui.screens.createworkout

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.workouts.WorkoutCategory
import com.example.gymappsas.ui.AppTheme
import com.example.gymappsas.ui.reusable.LoadingCircle
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.util.GetImagePath
import com.example.gymappsas.util.MockExerciseData
import com.example.gymappsas.util.StyledInputField
import com.example.gymappsas.util.TopAlignedInputField

@Composable
fun CreateWorkoutScreen(
    createWorkoutViewModel: CreateWorkoutViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSelectedCategory: (List<String>) -> Unit,
    onWorkoutTitleChange: (String) -> Unit,
    onWorkoutDescriptionChange: (String) -> Unit,
    navigateToExerciseDetails: (Long) -> Unit,
    navigateToAddExerciseToWorkoutStep: () -> Unit,
    onWorkoutCategoryChange: (WorkoutCategory) -> Unit
) {
    val uiState by createWorkoutViewModel.uiState.collectAsState()

    if (!uiState.isLoading) {
        Crossfade(uiState.createWorkoutStep, label = "profile setup") { screen ->
            when (screen) {
                CreateWorkoutStep.ADDEXERCISES -> {
                    ChooseExercisesScreen(
                        categories = uiState.selectedCategories,
                        exerciseList = uiState.exercises,
                        onSelectedExerciseChange = {
                            createWorkoutViewModel.toggleExerciseSelection(
                                it
                            )
                        },
                        navigateToExerciseDetails = { navigateToExerciseDetails(it) },
                        selectedExercises = uiState.selectedExercises,
                        onSaveWorkoutClick = { createWorkoutViewModel.createWorkout() },
                    )
                }

                CreateWorkoutStep.COMPLETE -> {
                    WorkoutCreatedScreen(
                        onAddToSchedule = { },
                        onStartNow = { },
                        onShare = { },
                        onBackToWorkouts = { onBackClick() }
                    )
                }

                CreateWorkoutStep.WORKOUTDETAILS -> {
                    Content(
                        onBackClick = { onBackClick() },
                        uiState = uiState,
                        onSelectedCategory = onSelectedCategory,
                        onWorkoutTitleChange = { onWorkoutTitleChange(it) },
                        onWorkoutDescriptionChange = { onWorkoutDescriptionChange(it) },
                        navigateToNextStep = { navigateToAddExerciseToWorkoutStep() },
                        onWorkoutCategoryChange = { onWorkoutCategoryChange(it) }
                    )

                }
            }
        }
    } else {
        LoadingCircle()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Content(
    onBackClick: () -> Unit,
    uiState: CreateWorkoutUiState,
    onSelectedCategory: (List<String>) -> Unit,
    onWorkoutTitleChange: (String) -> Unit,
    onWorkoutDescriptionChange: (String) -> Unit,
    navigateToNextStep: () -> Unit,
    onWorkoutCategoryChange: (WorkoutCategory) -> Unit
) {

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF7FAFC)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Create Workout",
                        color = Color(0xFF3B82F6),
                        fontSize = 17.sp,
                        fontFamily = lexenBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Workout Name",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = lexendRegular,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            StyledInputField(
                modifier = Modifier.padding(horizontal = 16.dp),
                input = uiState.workoutTitle,
                onInputChange = onWorkoutTitleChange,
                placeholder = "E.g., Full Body Workout",
                hasErrors = uiState.hasTitleError,
                errorMessage = uiState.titleErrorMessage
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Select Workout Type",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = lexendRegular,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                WorkoutCategory.entries.forEach { type ->
                    val isSelected = uiState.selectedCategory?.displayName.equals(type.displayName)
                    FilterChip(
                        label = { Text(type.displayName) },
                        selected = isSelected,
                        onClick = { onWorkoutCategoryChange(type) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF3B82F6),
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF3F4F6),
                            labelColor = Color.Black
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Description",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = lexendRegular,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TopAlignedInputField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(80.dp),
                input = uiState.workoutDescription,
                onInputChange = onWorkoutDescriptionChange,
                placeholder = "Workout description",
                hasErrors = uiState.hasDescriptionError,
                errorMessage = uiState.descriptionErrorMessage
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Select categories for workout",
                fontSize = 16.sp,
                fontFamily = lexendRegular,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CheckboxMinimalExample(
                exerciseCategories = uiState.exerciseCategories.map { it -> it.name.replaceFirstChar { it.uppercaseChar() } },
                onSelectedCategoriesChanged = { onSelectedCategory(it) })
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(Color(0xFF3B82F6))
                    .clickable { navigateToNextStep() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Continue",
                    fontFamily = lexenBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp),
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun ContentPrev() {
    AppTheme(dynamicColor = false) {

        Content(
            onBackClick = {},
            uiState = CreateWorkoutUiState(),
            onSelectedCategory = {},
            onWorkoutDescriptionChange = {},
            onWorkoutTitleChange = {},
            navigateToNextStep = {},
            onWorkoutCategoryChange = {}
        )
    }
}

@Preview
@Composable
private fun AddExercisesToWorkoutPreview() {
    AppTheme(dynamicColor = false) {
        ChooseExercisesScreen(
            categories = listOf("Chest", "Back", "Legs"),
            exerciseList = MockExerciseData.mockExercises,
            onSelectedExerciseChange = {},
            navigateToExerciseDetails = {},
            selectedExercises = emptyList(),
            onSaveWorkoutClick = {},
            isLoading = false
        )
    }
}

@Composable
fun CheckboxMinimalExample(
    exerciseCategories: List<String>,
    onSelectedCategoriesChanged: (List<String>) -> Unit
) {
    val selectedCategories = remember { mutableStateListOf<String>() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val halfSize = (exerciseCategories.size + 1) / 2
        val firstColumnItems = exerciseCategories.take(halfSize)
        val secondColumnItems = exerciseCategories.drop(halfSize)

        Column(modifier = Modifier.weight(1f)) {
            firstColumnItems.forEach { exerciseCategory ->
                val isChecked = selectedCategories.contains(exerciseCategory)
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 1.dp)) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedCategories.add(exerciseCategory)
                            } else {
                                selectedCategories.remove(exerciseCategory)
                            }
                            onSelectedCategoriesChanged(selectedCategories.toList())
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF3B82F6),
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = exerciseCategory,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = lexendRegular,
                        color = Color.Black
                    )
                }
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            secondColumnItems.forEach { exerciseCategory ->
                val isChecked = selectedCategories.contains(exerciseCategory)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedCategories.add(exerciseCategory)
                            } else {
                                selectedCategories.remove(exerciseCategory)
                            }
                            onSelectedCategoriesChanged(selectedCategories.toList())
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF3B82F6),
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = exerciseCategory,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = lexendRegular,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckBoxPreview() {
    CheckboxMinimalExample(exerciseCategories = listOf(ExerciseCategory.getAllCategories().toString()), {})
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChooseExercisesScreen(
    categories: List<String>,
    exerciseList: List<Exercise>,
    onSelectedExerciseChange: (Exercise) -> Unit,
    navigateToExerciseDetails: (Long) -> Unit,
    selectedExercises: List<Exercise>,
    onSaveWorkoutClick: () -> Unit,
    isLoading: Boolean = false
) {
    Surface(color = Color(0xFFF7FAFC)) {
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
                            ItemList(
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
                        .background(Color(0xFF3B82F6))
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
                        color = Color.White
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

@Composable
fun ItemList(
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
                    text = exercise.name,
                    fontFamily = lexendRegular,
                    color = Color.Black
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
                    onCheckedChange = { onExerciseCheckedChange(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF3B82F6),
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White
                    )
                )
            }
        )
        HorizontalDivider(color = Color(0xFFF3F4F6))
    }
}

@Composable
private fun CategoryHeader(
    headerText: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = headerText,
        fontFamily = lexenBold,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF7FAFC))
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
            ItemList(exercise = it, listOf(), {}, {})
        }
    }
}

@Preview
@Composable
private fun StickyListPreview() {
    CategorizedLazyColumn(category = "Kategorija", exercises = MockExerciseData.mockExercises)
}

@Composable
fun WorkoutCreatedScreen(
    onAddToSchedule: () -> Unit,
    onStartNow: () -> Unit,
    onShare: () -> Unit,
    onBackToWorkouts: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFC))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.celebrateicon),
            contentDescription = "Workout Created",
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Great Work! 🎉",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            "You've created your first workout. Ready to get started?",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        // Workout Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAFC))
        ) {
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        Button(
            onClick = onAddToSchedule,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add to Schedule", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onStartNow,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Start Now", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onShare,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFF7FAFC),
                contentColor = Color.Black
            )
        ) {
            Icon(Icons.Default.Share, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Share Workout", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackToWorkouts) {
            Text("Back to Workouts", color = Color.Gray)
        }
    }
}

@Preview
@Composable
private fun WorkoutCreatedPreview() {
    WorkoutCreatedScreen(
        onAddToSchedule = {},
        onStartNow = {},
        onShare = {},
        onBackToWorkouts = {}
    )
}