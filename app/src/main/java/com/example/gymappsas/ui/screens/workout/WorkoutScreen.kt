package com.example.gymappsas.ui.screens.workout

import BaseScreen
import BaseScreenNavigation
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.gymappsas.R
import com.example.gymappsas.ui.reusable.LoadingCircle
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.util.GetImagePath
import com.example.gymappsas.util.MockWorkoutData

@Composable
fun WorkoutScreen(
    workoutViewModel: WorkoutViewModel,
    onBackClick: () -> Unit,
    onCreateWorkoutClick: () -> Unit,
    onClickSeeMore: () -> Unit,
    onSelectWorkoutClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onSearchBarTextValueChange: (String) -> Unit,
    navigate : (BaseScreenNavigation) -> Unit
) {
    val workoutUiState by workoutViewModel.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    if (workoutUiState.isLoading) {
        LoadingCircle()
    } else {
            Content(
                workoutUiState = workoutUiState,
                onBackClick = onBackClick,
                onCreateWorkoutClick = onCreateWorkoutClick,
                onClickSeeMore = onClickSeeMore,
                onSelectWorkoutClick = { onSelectWorkoutClick(it) },
                onDeleteClick = { onDeleteClick(it) },
                scrollState = scrollState,
                onValueChange = { onSearchBarTextValueChange(it) },
                navigate = { navigate(it)}
            )
        }
    }

@Composable
private fun Content(
    workoutUiState: WorkoutUiState,
    onBackClick: () -> Unit,
    onCreateWorkoutClick: () -> Unit,
    onClickSeeMore: () -> Unit,
    onSelectWorkoutClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    scrollState: LazyListState,
    onValueChange: (String) -> Unit,
    navigate : (BaseScreenNavigation) -> Unit
) {
    val focusManager = LocalFocusManager.current
    BaseScreen(isLoading = false, topBarTitle = "Workout", navigate = navigate) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(bottom = 58.dp)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            state = scrollState
        ) {
            item {
                SearchBar(
                    workoutUiState = workoutUiState, onValueChange = { onValueChange(it) },
                    focusManager = focusManager
                )
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            item {
                CreateWorkoutParallax(scrollPosition = remember { derivedStateOf { scrollState.firstVisibleItemScrollOffset } }) { onCreateWorkoutClick() }
            }
            item {
                FeaturedWorkoutsSection()
            }
            items(workoutUiState.filteredWorkouts) { workout ->
                WorkoutItem(
                    imageUrl = GetImagePath.getExerciseImagePath(
                        workout.exerciseWorkouts.first().exercise.primaryMuscles.first(),
                        exerciseName = workout.exerciseWorkouts.first().exercise.name
                    ),
                    title = workout.title,
                    description = workout.description,
                    workoutId = workout.id,
                    onSelectWorkoutClick = { onSelectWorkoutClick(workout.id) },
                    onDeleteClick = { onDeleteClick(workout.id) }
                )
            }
        }
    }
}


@Composable
fun SearchBar(
    workoutUiState: WorkoutUiState, onValueChange: (String) -> Unit,
    focusManager: FocusManager
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFE7EDF4), RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF49709c),
                modifier = Modifier.padding(start = 12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(value = workoutUiState.searchText,
                onValueChange = { onValueChange(it) },
                placeholder = {
                    Text(
                        text = "Search",
                        color = Color(0xFF49709c),
                        fontSize = 14.sp,
                        fontFamily = lexendRegular
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .background(Color.Transparent),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF0d141c),
                    unfocusedTextColor = Color(0xFF0d141c),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color(0xFF0d141c),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color(0xFF49709c),
                    unfocusedPlaceholderColor = Color(0xFF49709c)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
        }
    }
}

@Composable
fun FeaturedWorkoutsSection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Featured Workouts",
            color = Color(0xFF0d141c),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
            fontFamily = lexenBold // Use Lexend
        )
    }
}


@Composable
fun WorkoutItem(
    imageUrl: String,
    title: String,
    description: String,
    onSelectWorkoutClick: (Long) -> Unit,
    workoutId: Long,
    onDeleteClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(6.dp, RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0x99000000)),
                            startY = 50f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = lexenBold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }

        // Text Section with Elevated Style
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF607D8B),
                    fontWeight = FontWeight.Normal,
                    fontFamily = lexendRegular,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Learn More",
                        color = Color(0xFF2196F3),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onSelectWorkoutClick(workoutId) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    val mockWorkoutUiState = WorkoutUiState(
        isLoading = false,
        workouts = MockWorkoutData.mockWorkouts
    )
    Content(
        workoutUiState = mockWorkoutUiState,
        onBackClick = {},
        onCreateWorkoutClick = {},
        onClickSeeMore = {},
        onSelectWorkoutClick = {},
        onDeleteClick = {},
        scrollState = LazyListState(),
        onValueChange = {},
        navigate = {}
    )
}

@Composable
private fun CreateWorkoutParallax(scrollPosition: State<Int>, onCreateWorkoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(201.dp)
                .clip(RoundedCornerShape(12.dp))
                .graphicsLayer {
                    translationY = (scrollPosition.value * 0.3f)
                }
        ) {
            Image(
                painter = painterResource(R.drawable.workoutscreen),
                contentDescription = "Avatar icon",
                modifier = Modifier.matchParentSize()
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Create Your Own Workout",
                    fontSize = 18.sp,
                    fontFamily = lexenBold,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    "Personalize your fitness routine by creating a workout tailored to your needs",
                    fontSize = 14.sp,
                    fontFamily = lexendRegular,
                    color = Color(0xFF4A709C),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0A6BD9))
                    .clickable { onCreateWorkoutClick() }
            ) {
                Text(
                    "Create",
                    fontSize = 16.sp,
                    fontFamily = lexendRegular,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .padding(vertical = 5.5.dp)
                )
            }

        }
    }

}

@Preview
@Composable
private fun WorkoutItemPrev() {
    val mockWorkout = MockWorkoutData.mockWorkout
    WorkoutItem(
        title = mockWorkout.title,
        imageUrl = "",
        description = mockWorkout.description,
        onSelectWorkoutClick = {},
        onDeleteClick = {},
        workoutId = 0L
    )

}