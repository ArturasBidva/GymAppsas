package com.example.gymappsas.ui.screens.exercisesbyselectedcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.util.GetImagePath
import com.example.gymappsas.util.MockExerciseData

@Composable
fun ExercisesBySelectedCategory(
    exerciseBySelectedCategoryViewModel: ExerciseBySelectedCategoryViewModel,
    onNavigateBack: () -> Unit,
    onSelectExerciseClick: (Long) -> Unit
) {
    val uiState by exerciseBySelectedCategoryViewModel.uiState.collectAsState()
    Content(
        onSelectExerciseClick = { onSelectExerciseClick(it) },
        onDeleteClick = {},
        exerciseList = uiState.exercisesByCategory,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun Content(
    onSelectExerciseClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    exerciseList: List<Exercise>,
    onNavigateBack: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(bottom = 58.dp)) {
            Text(
                text = "Featured Exercises",
                color = Color(0xFF0d141c),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp),
                fontFamily = lexenBold // Use Lexend
            )
            exerciseList.forEach {
                ExerciseItem(
                    exercise = it,
                    onSelectExerciseClick = onSelectExerciseClick,
                    onDeleteClick = onDeleteClick,
                )
            }
        }
    }
}

@Composable
fun ExerciseItem(
    exercise: Exercise,
    onSelectExerciseClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp)
            .clickable { onSelectExerciseClick(exercise.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = GetImagePath.getExerciseImagePath(
            category = exercise.primaryMuscles.first(),
            exerciseName = exercise.name
        )
        Box(
            modifier = Modifier
                .size(width = 124.dp, height = 70.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(8.dp)),
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = exercise.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color(0xFF0D141C),
                fontFamily = lexendRegular
            )
            Text(
                text = exercise.instructions[0],
                fontSize = 14.sp,
                color = Color(0xFF49709C),
                fontFamily = lexendRegular,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(
        onDeleteClick = {},
        onSelectExerciseClick = {},
        exerciseList = MockExerciseData.mockExercises,
        onNavigateBack = {}
    )
}