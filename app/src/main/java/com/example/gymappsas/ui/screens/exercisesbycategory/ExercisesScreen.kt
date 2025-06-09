package com.example.gymappsas.ui.screens.exercisesbycategory

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.ui.reusable.LoadingCircle
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.ui.screens.workout.WorkoutViewModel
import com.example.gymappsas.util.MockExerciseData

@Composable
fun ExercisesScreen(
    exerciseViewModel: ExerciseViewModel,
    workoutViewModel: WorkoutViewModel,
    navigateToCreateExercise: () -> Unit,
    popStackBack: () -> Unit,
    navigateToExercisesByCategoryScreen: (String) -> Unit
) {
    val exerciseUiState by exerciseViewModel.uiState.collectAsState()
    val workoutUiState by workoutViewModel.uiState.collectAsState()

    if (exerciseUiState.isLoading || workoutUiState.isLoading) {
        LoadingCircle()
    } else {
        Content(
            exerciseUiState = exerciseUiState,
            onCreateExerciseClick = navigateToCreateExercise,
            onBackClick = { popStackBack() },
            navigateToExercisesByCategoryScreen = { navigateToExercisesByCategoryScreen(it) }
        )
    }
}

@Composable
private fun Content(
    exerciseUiState: ExerciseUiState,
    onCreateExerciseClick: () -> Unit,
    onBackClick: () -> Unit,
    navigateToExercisesByCategoryScreen: (String) -> Unit
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        ExerciseScreen(
            onCreateExerciseClick = onCreateExerciseClick,
            onBackClick = onBackClick,
            categories = exerciseUiState.exerciseCategories,
            exerciseList = exerciseUiState.exercises,
            navigateToExercisesByCategoryScreen = { navigateToExercisesByCategoryScreen(it) }
        )
    }
}


@Composable
fun ExerciseScreen(
    onCreateExerciseClick: () -> Unit,
    onBackClick: () -> Unit,
    categories : List<ExerciseCategory>,
    exerciseList: List<Exercise>,
    navigateToExercisesByCategoryScreen: (String) -> Unit
) {
    // Main layout
    Box(
        modifier = Modifier
            .background(Color(0xFFF8FAFC))
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 58.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            CreateExerciseParallax(scrollState.value, onCreateExerciseClick = onCreateExerciseClick)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Select a category",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(16.dp)
            )

            categories.forEach { category ->
                val exercisesInCategory = exerciseList.filter { exercise ->
                    exercise.primaryMuscles.any { it == category.name }
                }
                println("Category: ${category.name}, Exercises Found: ${exercisesInCategory.size}")
                val exercisesCount = exercisesInCategory.size

                CategoryItem(
                    title = category.name.replaceFirstChar { it.uppercaseChar() },
                    subtitle = "$exercisesCount Exercises",
                    navigateToExercisesByCategoryScreen = {
                        navigateToExercisesByCategoryScreen(
                            category.name
                        )
                    }
                )
            }

        }
    }
}

@Composable
fun CategoryItem(title: String, subtitle: String, navigateToExercisesByCategoryScreen: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navigateToExercisesByCategoryScreen() }
            .background(Color(0xFFF8FAFC)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                color = Color(0xFF0D141C),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(text = subtitle, color = Color(0xFF49709C), fontSize = 14.sp)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Arrow Right",
            tint = Color(0xFF0D141C)
        )
    }

    
}

@Preview
@Composable
private fun CategoryItemPreview() {
    CategoryItem("Kazas","Belekas",{})
}

@Composable
private fun CreateExerciseParallax(int: Int, onCreateExerciseClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(201.dp)
                .clip(RoundedCornerShape(12.dp))
                .graphicsLayer {
                    translationY = 0.5f * int
                }
        ) {
            Image(
                painter = painterResource(R.drawable.create_exercise_picture),
                contentDescription = "Avatar icon",
                modifier = Modifier.matchParentSize()
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Create New Exercise",
                    fontSize = 18.sp,
                    fontFamily = lexenBold,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    "Upload an image and add details",
                    fontSize = 16.sp,
                    fontFamily = lexendRegular,
                    color = Color(0xFF4A709C),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0A6BD9))
                    .clickable { onCreateExerciseClick() }
            ) {
                Text(
                    "Create Exercise",
                    fontSize = 14.sp,
                    fontFamily = lexendRegular,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 5.5.dp)
                )
            }

        }
    }

}

@Preview
@Composable
private fun ExerciseScreenPreview() {
    ExerciseScreen(
        onCreateExerciseClick = {},
        onBackClick = {},
        categories = ExerciseCategory.getAllCategories(),
        exerciseList = MockExerciseData.mockExercises,
        navigateToExercisesByCategoryScreen = {}
    )
}
















