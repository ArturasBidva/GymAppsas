package com.example.gymappsas.ui.screens.profilesetup

import CustomButton
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappsas.R
import com.example.gymappsas.ui.AppTheme


@Composable
fun GoalScreen(
    profileRegistrationUiState: ProfileRegistrationUiState,
    onGoalSelected: (FitnessGoal) -> Unit,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    val selectedGoal = profileRegistrationUiState.selectedFitnessGoal
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header section with step indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StepIndicator(
                    currentStep = profileRegistrationUiState.currentStep,
                    stepCount = profileRegistrationUiState.totalSteps,
                    title = "Set your goal",
                    onBack = onBack
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF6C63FF),
                                    Color(0xFF9C88FF)
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.goal_icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "What's your goal?",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 22.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Text(
                    text = "We'll customize your workouts based on your goals",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 2.dp)
            ) {
                items(FitnessGoal.entries) { goal ->
                    GoalOptionCard(
                        goal = goal,
                        isSelected = selectedGoal == goal,
                        onSelect = { onGoalSelected(goal) }
                    )
                }
            }

            CustomButton(onContinueClick = onContinue)
        }
    }
}

@Composable
fun GoalOptionCard(
    modifier: Modifier = Modifier,
    goal: FitnessGoal,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val cardShape = RoundedCornerShape(20.dp)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(cardShape)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) goal.selectedBorderColor else Color(0xFFE5E7EB),
                shape = cardShape
            )
            .clickable { onSelect() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) goal.selectedBackgroundColor else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp,
            pressedElevation = 2.dp,
            focusedElevation = 2.dp,
            hoveredElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon with background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isSelected) goal.primaryColor else goal.backgroundColor,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(goal.icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) Color.White else goal.primaryColor
                )
            }

            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = goal.displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = goal.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    painter = painterResource(R.drawable.icon_1),
                    contentDescription = "Selected",
                    modifier = Modifier.size(14.dp),
                    tint = goal.primaryColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun GoalScreenPreview() {
    AppTheme {
        GoalScreen(
            onGoalSelected = {},
            onContinue = {},
            profileRegistrationUiState = ProfileRegistrationUiState(selectedFitnessGoal =
                FitnessGoal.BUILD_MUSCLE),
            onBack = {}
        )
    }
}

@Preview
@Composable
private fun GoalScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        GoalScreen(
            onGoalSelected = {},
            onContinue = {},
            profileRegistrationUiState = ProfileRegistrationUiState(selectedFitnessGoal =
                FitnessGoal.LOSE_WEIGHT),
            onBack = {}
        )
    }
}
