package com.example.gymappsas.ui.screens.exercisesbyselectedcategory

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.gymappsas.data.db.models.exercisecategory.ExerciseCategory
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.util.GetImagePath.getExerciseImagePath
import com.example.gymappsas.util.MockExerciseData

@Composable
fun ExercisesBySelectedCategory(
    exerciseBySelectedCategoryViewModel: ExerciseBySelectedCategoryViewModel,
    onNavigateBack: () -> Unit,
    onSelectExerciseClick: (Long) -> Unit
) {
    val uiState by exerciseBySelectedCategoryViewModel.uiState.collectAsState()
    Content(
        exerciseList = uiState.exercisesByCategory,
        onNavigateBack = onNavigateBack,
        onExerciseClick = { onSelectExerciseClick(it) }
    )
}

@Composable
private fun Content(
    exerciseList: List<Exercise>,
    onNavigateBack: () -> Unit = {},
    onExerciseClick: (Long) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF7FAFC)
    ) {
        CategoryExercisesScreen(
            categoryName = exerciseList.firstOrNull()?.primaryMuscles?.firstOrNull() ?: "Unknown",
            onExerciseClick = { onExerciseClick(it) },
            onBack = { onNavigateBack() },
            exercises = exerciseList,
            categoryIcon = ExerciseCategory.getCategoryEmoji(
                exerciseList.firstOrNull()?.primaryMuscles?.firstOrNull() ?: "Unknown"
            ),
            exerciseCount = exerciseList.size,
        )
    }
}

@Composable
fun CategoryExercisesScreen(
    categoryName: String,
    categoryIcon: String,
    exerciseCount: Int,
    exercises: List<Exercise>,
    onBack: () -> Unit,
    onExerciseClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 18.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = categoryIcon,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Column {
                    Text(
                        text = categoryName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "$exerciseCount exercises",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // List of exercises
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            exercises.forEach { exercise ->
                ExerciseCard(exercise = exercise, onClick = { onExerciseClick(exercise.id) })
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = getExerciseImagePath(
            category = exercise.primaryMuscles.first(),
            exerciseName = exercise.name
        )
        AsyncImage(
            model = imageUrl,
            contentDescription = exercise.name,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = exercise.name,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = exercise.primaryMuscles.first(),
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Tag(text = exercise.equipment)
                Tag(text = exercise.level)
            }
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Go",
            tint = Color.Gray,
            modifier = Modifier.size(26.dp)
        )
    }
}



@Composable
fun Tag(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFF7FAFC), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(
        exerciseList = MockExerciseData.mockExercises,
        onNavigateBack = { },
        onExerciseClick = {}
    )
}