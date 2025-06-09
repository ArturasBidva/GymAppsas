package com.example.gymappsas.ui.screens.exercisedetails

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.notosands
import com.example.gymappsas.util.GetImagePath.getExerciseImagePath
import com.example.gymappsas.util.MockExerciseData

@Composable
fun ExerciseDetails(exerciseDetailsViewModel: ExerciseDetailsViewModel, popStackBack: () -> Unit) {
    val uiState by exerciseDetailsViewModel.uiState.collectAsState()
    uiState.selectedExercise?.let {
        Content(
        modifier = Modifier, selectedExercise = it, popStackBack = popStackBack
    )
    }

}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    selectedExercise: Exercise,
    popStackBack: () -> Unit
) {
    Surface(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(modifier = Modifier.padding(bottom = 58.dp)) {
            ExerciseBannerSection(selectedExercise = selectedExercise)
            Text(
                text = selectedExercise.name,
                fontFamily = lexenBold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111418),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
            )
            Text(
                text = "Fitness Pilates is a great way to build a strong and stable core, improving posture.",
                fontFamily = notosands,
                fontSize = 16.sp,
                color = Color(0xFF111418),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "How to do it",
                fontFamily = lexenBold,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111418),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
            )

            // Display steps
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                selectedExercise.instructions.forEachIndexed { index, instruction ->
                    StepItem(
                        icon = Icons.Default.Star,  // Default icon for each instruction
                        text = instruction,
                        isLastStep = index == selectedExercise.instructions.lastIndex
                    )
                }
            }
        }
    }
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
fun ExerciseBannerSection(selectedExercise: Exercise, imageCount: Int = 2) {
    val pagerState = rememberPagerState(pageCount = { imageCount })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(218.dp)
            .background(Color.LightGray)
    ) {
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
                    .clip(RoundedCornerShape(8.dp)),
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
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
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
fun StepItem(
    icon: ImageVector,
    text: String,
    isLastStep: Boolean
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF007AFF),
            modifier = Modifier
                .size(24.dp)
                .background(Color.White, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Text
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Black,
            lineHeight = 20.sp
        )
    }

    if (!isLastStep) {
        // Dashed Line
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .padding(start = 12.dp)
                .width(1.dp)
                .height(16.dp)
                .background(Color.Gray.copy(alpha = 0.5f))
        )
    }
}
