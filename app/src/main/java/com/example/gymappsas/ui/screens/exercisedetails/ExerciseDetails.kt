package com.example.gymappsas.ui.screens.exercisedetails

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.util.GetImagePath.getExerciseImagePath
import com.example.gymappsas.util.MockExerciseData

@Composable
fun ExerciseDetails(exerciseDetailsViewModel: ExerciseDetailsViewModel, popStackBack: () -> Unit) {
    val uiState by exerciseDetailsViewModel.uiState.collectAsState()
    uiState.selectedExercise?.let {
        Content(
            modifier = Modifier, selectedExercise = it, popStackBack = { popStackBack() }
        )
    }

}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    selectedExercise: Exercise,
    popStackBack: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(bottom = 58.dp)) {
            ExerciseBannerSection(
                selectedExercise = selectedExercise,
                popStackBack = { popStackBack() }
            )

            // Title
            Text(
                text = selectedExercise.name,
                fontFamily = lexenBold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
            )

            // Tags (pills)
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Pill(text = selectedExercise.primaryMuscles.firstOrNull() ?: "")
                Pill(text = selectedExercise.equipment)
                Pill(text = selectedExercise.level)
            }
            TextSectionHeader("Instructions")
            InstructionList(instructions = selectedExercise.instructions)
        }
    }
}

@Composable
fun Pill(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        fontWeight = FontWeight.SemiBold
    )
}

@Preview
@Composable
private fun ContentPrev() {
    Content(
        modifier = Modifier,
        selectedExercise = MockExerciseData.mockExercises[0],
        popStackBack = {}
    )
}


@Composable
fun ExerciseBannerSection(
    selectedExercise: Exercise,
    imageCount: Int = 2,
    popStackBack: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { imageCount })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(218.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        IconButton(
            onClick = popStackBack,
            modifier = Modifier
                .padding(16.dp)
                .size(36.dp)
                .align(Alignment.TopStart)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), CircleShape)
                .clickable { popStackBack() }
                .zIndex(1f)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.matchParentSize()
        ) { page ->
            val imageUrl = getExerciseImagePath(
                category = selectedExercise.primaryMuscles.first(),
                exerciseName = selectedExercise.name,
                imageIndex = page
            )
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(imageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }
}

@Composable
fun TextSectionHeader(text: String) {
    Text(
        text = text,
        fontFamily = lexenBold,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Composable
fun InstructionList(instructions: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        instructions.forEachIndexed { index, step ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = step, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
