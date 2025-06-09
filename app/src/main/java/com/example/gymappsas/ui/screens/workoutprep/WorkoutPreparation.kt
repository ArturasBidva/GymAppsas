package com.example.gymappsas.ui.screens.workoutprep

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendMedium
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.util.MockExerciseWorkoutData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun WorkoutPreparationScreen(
    viewModel: WorkoutPreparationViewModel,
    onSetClick: (Long) -> Unit,
    getSelectedWeight: (String) -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    val workout = state.value.selectedWorkout
    if (workout != null) {
        val exercises = workout.exerciseWorkouts
        val totalSteps = exercises.size
        val weightList = (5..160 step 5).map { "$it kg" }
        Content(
            exercises = exercises,
            onSetClick = { onSetClick(it.id) },
            progress = state.value.progress,
            totalSteps = totalSteps,
            onContinueClick = { println("Continue clicked") },
            isContinueEnabled = state.value.progress == totalSteps,
            onBackClick = { onBackClick() }
        )
        if (state.value.isSelectWeightDialogOpen) {
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxSize()
            ) {}
            ExerciseWeightDialog(
                weightList = weightList,
                selectedWeight = { getSelectedWeight(it) },
                onCancelClick = { onCancelClick() },
                onConfirmClick = { onConfirmClick() }
            )
        }
    }
}


@Composable
private fun Content(
    exercises: List<ExerciseWorkout>,
    onSetClick: (ExerciseWorkout) -> Unit,
    progress: Int,
    totalSteps: Int,
    onContinueClick: () -> Unit,
    isContinueEnabled: Boolean,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 58.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Prepare for your workout",
                color = Color.Black,
                fontFamily = lexenBold,
                fontSize = 28.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Welcome to the setup process for your workout. We'll help you select the right weights for each exercise.",
                color = Color.Black,
                fontFamily = lexenBold,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Subtitle
            Text(
                text = "Maximum Manageable Weight",
                color = Color.Black,
                fontFamily = lexenBold,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            exercises.forEach { exercise ->
                val visible by rememberUpdatedState(exercise.weight == 0)
                val density = LocalDensity.current
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically {
                        with(density) { -40.dp.roundToPx() }
                    } + expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    ExerciseItem(
                        exerciseWorkout = exercise,
                        onSetClick = { onSetClick(exercise) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Setup Progress",
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = {
                    progress.toFloat() / totalSteps
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$progress out of $totalSteps",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onContinueClick,
                enabled = isContinueEnabled,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Continue")
            }
        }

    }
}

@Composable
private fun ExerciseItem(
    exerciseWorkout: ExerciseWorkout,
    onSetClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = exerciseWorkout.exercise.name,
                fontFamily = lexendMedium,
                fontSize = 16.sp
            )
            Text(
                text = "Choose a weight that you can lift 8 times with good form",
                color = Color.Gray,
                fontFamily = lexendRegular,
                fontSize = 14.sp
            )
        }
        TextButton(onClick = { onSetClick(exerciseWorkout.id) }) {
            Text(text = "Set")
        }
    }
}

@Preview
@Composable
private fun MainScreenPrev() {
    val dialogVisible = remember { mutableStateOf(false) }

    Content(
        exercises = MockExerciseWorkoutData.mockExerciseWorkouts,
        onSetClick = {
            // When the Set button is clicked, show the dialog
            dialogVisible.value = true
        },
        progress = 2,
        totalSteps = 11,
        onContinueClick = {},
        isContinueEnabled = true,
        onBackClick = {}
    )

    if (dialogVisible.value) {
        ExerciseWeightDialog(
            weightList = (5..125 step 5).map { "$it kg" },
            selectedWeight = {},
            onCancelClick = { dialogVisible.value = false },
            onConfirmClick = { dialogVisible.value = false }
        )
    }
}

@Composable
fun Picker(
    modifier: Modifier = Modifier,
    items: List<String>,
    state: PickerState,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = LocalContentColor.current,

) {

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.intValue)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> index + visibleItemsMiddle }
            .distinctUntilChanged()
            .collect { adjustedIndex ->
                state.updateSelectedIndex(adjustedIndex % items.size)
            }
    }

    Box(modifier = modifier) {

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                        .then(textModifier),
                    fontFamily = lexenBold
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.offset(y = itemHeightDp * visibleItemsMiddle),
            color = dividerColor
        )

        HorizontalDivider(
            modifier = Modifier.offset(y = itemHeightDp * (visibleItemsMiddle + 1)),
            color = dividerColor
        )

    }

}


private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@Composable
fun ExerciseWeightDialog(
    weightList: List<String>,
    selectedWeight: (String) -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Dialog(onDismissRequest = { onCancelClick() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E1E2C), // Dark background
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val unitsPickerState = rememberPickerState(initialIndex = 0, items = weightList)

                // Dialog Title
                Text(
                    text = "Select Max Weight",
                    fontFamily = lexenBold,
                    fontSize = 24.sp,
                    color = Color(0xFFFFFFFF), // White text
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // Weight Picker
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Picker(
                        state = unitsPickerState,
                        items = weightList,
                        visibleItemsCount = 5,
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(vertical = 16.dp),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(
                            fontSize = 32.sp,
                            color = Color(0xFFFFFFFF)
                        ) // White picker text
                    )
                }

                // Selected Weight Display
                Text(
                    text = "Selected weight: ${unitsPickerState.selectedItem} kg",
                    color = Color(0xFFAAAAAA), // Light gray
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { onCancelClick() }) {
                        Text(
                            text = "Cancel",
                            color = Color(0xFFFF5252),
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = {
                            selectedWeight(unitsPickerState.selectedItem)
                            onConfirmClick()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text(
                            text = "Confirm",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ExerciseWeightDialogPreview() {
    val weightList = (5..160 step 5).map { "$it kg" }
    ExerciseWeightDialog(
        weightList = weightList,
        selectedWeight = {},
        onCancelClick = {},
        onConfirmClick = {}
    )
}