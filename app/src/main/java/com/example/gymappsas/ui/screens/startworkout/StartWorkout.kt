package com.example.gymappsas.ui.screens.startworkout

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.ui.reusable.montserrati
import com.example.gymappsas.util.MockWorkoutData
import com.example.gymappsas.util.TimerComposable

@Composable
fun StartWorkout(viewModel: StartWorkoutViewModel, onWorkoutComplete: (Workout) -> Unit) {

}

@Composable
private fun Content(
    selectedWorkout: Workout,
    onWorkoutComplete: (Workout) -> Unit,
    weeklyWorkoutCount: Int
) {
    var currentCount by remember { mutableIntStateOf(0) }
    var exerciseCount by remember { mutableIntStateOf(0) }
    val exerciseList = selectedWorkout.exerciseWorkouts
    var exercise by remember { mutableStateOf(exerciseList[exerciseCount]) }
    val goalCount = exercise.goal
    var isWorkoutCompleted by remember {
        mutableStateOf(false)
    }

    val incrementCount = {
        if (currentCount < goalCount - 1) {
            currentCount++
        } else {
            currentCount = 0
            if (exerciseCount < exerciseList.size - 1) {
                exerciseCount++
                exercise = exerciseList[exerciseCount]
            } else {
                isWorkoutCompleted = true
            }
        }
    }
    val decrementCount = {
        if (currentCount > 0) {
            currentCount--
        } else {
            if (exerciseCount > 0) {
                exerciseCount--
                exercise = exerciseList[exerciseCount]
                currentCount = exercise.goal - 1
            } else {

            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        if (!isWorkoutCompleted) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp + 56.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                WorkoutInfo(selectedExercise = exercise, selectedWorkout = selectedWorkout)
                Spacer(modifier = Modifier.height(30.dp))
                ExerciseImage(selectedWorkout = selectedWorkout)
                Spacer(modifier = Modifier.height(40.dp))
                WorkoutControllers(
                    currentCount = currentCount,
                    goalCount = goalCount,
                    onNextSet = { incrementCount() },
                    onPreviousSet = { decrementCount() },
                    onTimerComplete = { incrementCount() }
                )
                Spacer(modifier = Modifier.height(124.dp))
            }
        } else {
            WorkoutCompleted(
                onWorkoutComplete = { onWorkoutComplete(selectedWorkout) },
                weeklyWorkoutCount = weeklyWorkoutCount
            )
        }
    }
}


@Preview
@Composable
private fun StartWorkoutPreview() {
    Content(
        selectedWorkout = MockWorkoutData.mockWorkouts[0],
        onWorkoutComplete = {},
        weeklyWorkoutCount = 2
    )
}

@Composable
fun WorkoutInfo(selectedExercise: ExerciseWorkout, selectedWorkout: Workout) {
    Text(
        text = selectedWorkout.title,
        fontFamily = montserrati,
        fontSize = 18.sp,
        color = Color(0XFF707070)
    )
    Spacer(modifier = Modifier.height(1.dp))
    Spacer(modifier = Modifier.height(1.dp))
    Text(
        text = "Set",
        fontFamily = montserrati,
        fontSize = 18.sp,
        color = Color(0XFF707070)
    )
    Text(
        text = selectedExercise.weight.toString(),
        fontFamily = montserrati,
        fontSize = 32.sp,
        color = Color.Black
    )
}


@Composable
fun ExerciseImage(selectedWorkout: Workout) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 95.dp)
            .height(120.dp)
            .clip(shape = RoundedCornerShape(8.dp))
    ) {


    }
}

@Composable
fun WorkoutControllers(
    currentCount: Int,
    goalCount: Int,
    onNextSet: () -> Unit,
    onPreviousSet: () -> Unit,
    onTimerComplete: () -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until goalCount) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(if (i < currentCount) Color.Black else Color.Gray)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .clickable { onPreviousSet() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_left_1_svgrepo_com_1),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                TimerComposable(
                    totalTime = 3L * 1000L,
                    handleColor = Color.Green,
                    inactiveBarColor = Color.DarkGray,
                    activeBarColor = Color(0xFF37B900),
                    modifier = Modifier.size(200.dp),
                    onTimerFinish = { onTimerComplete() }
                )
                Box(
                    Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .clickable { onNextSet() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_right_1_svgrepo_com_2),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseInfo(exercise: Exercise) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = exercise.name,
                fontFamily = montserrati,
                fontSize = 32.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(Color.Blue, shape = CircleShape)
                    .clickable { showDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "?",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.graphicsLayer { translationY = 2.dp.toPx() },
                )
            }
        }
        if (showDialog) {
            Dialog(
                onDismissRequest = { showDialog = false }
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 50.dp)
                        .background(Color(0xFF464646), RoundedCornerShape(8.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "Chest",
                                fontSize = 12.sp,
                                color = Color(0xFFFF9B70),
                                textAlign = TextAlign.Start
                            )
                            Box(
                                Modifier
                                    .height(20.dp)
                                    .width(20.dp)
                                    .clickable { showDialog = false }
                            ) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.exiticon),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    colorFilter = ColorFilter.tint(Color(0xFFFF9B70))
                                )
                            }
                        }
                        Text(
                            text = "Bench press",
                            fontSize = 20.sp,
                            color = Color.White,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(Modifier.height(148.dp)) {
                            Image(
                                painter = painterResource(id = R.drawable.deadlift),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "This is the information about Bench Press exercise.",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}




@Composable
fun WorkoutCompleted(onWorkoutComplete: () -> Unit, weeklyWorkoutCount: Int) {
    Surface(modifier = Modifier.fillMaxSize()) {
        onWorkoutComplete()
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp + 56.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Congratulation you have done $weeklyWorkoutCount workout's this week!")
            }
        }

    }

}

@Preview
@Composable
private fun WorkoutCompletedPrev() {
    WorkoutCompleted({}, weeklyWorkoutCount = 2)
}