package com.example.gymappsas.ui.screens.mainscreen

import BaseScreen
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.ui.reusable.montserrati
import com.example.gymappsas.util.MockProfileData

@Composable
fun MainScreen(
    mainScreenViewModel: MainViewModel = hiltViewModel(),
    navigateToWorkout: () -> Unit,
    navigateToExercise: () -> Unit,
    navigateToWorkoutSchedule: () -> Unit,
    navigateToChooseWorkout: () -> Unit,
    navigateToWorkoutHistory: () -> Unit
) {
    val uiState by mainScreenViewModel.uiState.collectAsState(MainScreenUiState())

    Content(
        onWorkoutNavigateClick = navigateToWorkout,
        onExerciseNavigateClick = navigateToExercise,
        onWorkoutScheduleClick = navigateToWorkoutSchedule,
        onChooseWorkoutClick = navigateToChooseWorkout,
        completedWorkoutCount = uiState.completedWorkoutsWeeklyCounter,
        navigateToWorkoutHistory = navigateToWorkoutHistory,
        profile = uiState.profile
    )
}


@Composable
private fun Content(
    onWorkoutNavigateClick: () -> Unit,
    onExerciseNavigateClick: () -> Unit,
    onWorkoutScheduleClick: () -> Unit,
    onChooseWorkoutClick: () -> Unit,
    navigateToWorkoutHistory: () -> Unit,
    completedWorkoutCount: Int,
    profile: Profile
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Welcome ${profile.name},",
                fontFamily = montserrati,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 43.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 43.dp)) {
                Spacer(modifier = Modifier.padding(10.dp))
                Box(
                    Modifier
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(10.dp),
                            spotColor = MaterialTheme.colorScheme.outline
                        )
                        .height(90.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "This week you've done $completedWorkoutCount Workouts!",
                            fontSize = 24.sp,
                            fontFamily = montserrati,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(12.dp))
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(10.dp),
                                spotColor = MaterialTheme.colorScheme.outline
                            )
                            .weight(1f)
                            .height(90.dp)
                            .clickable { onWorkoutNavigateClick() }
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val avatar = painterResource(R.drawable.ion_fitness_sharp)
                            Image(
                                painter = avatar,
                                contentDescription = "Hearth",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                            )
                            Text(
                                text = "Your workouts",
                                fontFamily = montserrati,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(10.dp),
                                spotColor = MaterialTheme.colorScheme.outline
                            )
                            .weight(1f)
                            .height(90.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable { navigateToWorkoutHistory() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val avatar = painterResource(R.drawable.vector)
                            Image(
                                painter = avatar,
                                contentDescription = "History",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer)
                            )
                            Text(
                                text = "Workout history",
                                fontFamily = montserrati,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(10.dp),
                                spotColor = MaterialTheme.colorScheme.outline
                            )
                            .weight(1f)
                            .fillMaxWidth()
                            .height(90.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .clickable { /* Handle click */ },
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val avatar = Icons.Outlined.Clear
                            Image(
                                imageVector = avatar,
                                contentDescription = "Library",
                                modifier = Modifier.size(43.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiaryContainer)
                            )
                            Text(
                                text = "Workouts library",
                                fontFamily = montserrati,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(10.dp),
                                spotColor = MaterialTheme.colorScheme.outline
                            )
                            .weight(1f)
                            .fillMaxWidth()
                            .height(90.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .clickable { onExerciseNavigateClick() },
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val avatar = painterResource(R.drawable.exercisegym)
                            Image(
                                painter = avatar,
                                contentDescription = "Exercises",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onErrorContainer)
                            )
                            Text(
                                text = "Exercises",
                                fontFamily = montserrati,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    Modifier
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(10.dp),
                            spotColor = MaterialTheme.colorScheme.outline
                        )
                        .height(90.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.inversePrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable { onWorkoutScheduleClick() }
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = "Calendar icon",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(47.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Box(
                                Modifier
                                    .height(46.dp)
                            ) {
                                Text(
                                    text = "Make your workout schedule",
                                    fontSize = 16.sp,
                                    fontFamily = montserrati,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 12.dp))
                Box(
                    Modifier
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(10.dp),
                            spotColor = MaterialTheme.colorScheme.outline
                        )
                        .height(70.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable { onChooseWorkoutClick() },
                ) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val avatar = painterResource(R.drawable.barbell)
                        Icon(
                            painter = avatar,
                            contentDescription = "Barbell",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column {
                                Text(
                                    text = "Ready for workout?",
                                    fontSize = 14.sp,
                                    fontFamily = montserrati,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                                Text(
                                    text = "Click here to choose and start your daily workout!",
                                    fontSize = 10.sp,
                                    fontFamily = montserrati,
                                    lineHeight = 12.sp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPrev() {
    BaseScreen(
        isLoading = false, topBarTitle = "Home",
        content = {
            Content(
                onWorkoutNavigateClick = { /*TODO*/ },
                onExerciseNavigateClick = {},
                onWorkoutScheduleClick = {},
                onChooseWorkoutClick = {},
                completedWorkoutCount = 10,
                navigateToWorkoutHistory = {},
                profile = MockProfileData.mockProfile,
            )
        }
    )
}



