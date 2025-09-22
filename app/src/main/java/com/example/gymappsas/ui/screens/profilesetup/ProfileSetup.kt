package com.example.gymappsas.ui.screens.profilesetup

import CustomButton
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappsas.R
import com.example.gymappsas.ui.AppTheme
import com.example.gymappsas.util.Validator

@Composable
fun ProfileSetup(profileSetupViewModel: ProfileSetupViewModel) {
    val uiState by profileSetupViewModel.uiState.collectAsState()

    Crossfade(targetState = uiState.profileSetupStep, label = "cross fade") { screen ->
        when (screen) {
            ProfileSetupStep.NAME -> ProfileSetupScreen(
                uiState = uiState,
                onContinueClick = { profileSetupViewModel.proceedToGenderStep() },
                onGenderChange = { profileSetupViewModel.onGenderChanged(it) },
                onMetricChange = { type, value ->
                    profileSetupViewModel.onMetricChanged(
                        type,
                        value
                    )
                }
            )

            ProfileSetupStep.METRICS -> MetricsScreen(
                state = uiState,
                onContinueClick = { profileSetupViewModel.proceedToGoalStep() },
                onMetricChange = { type, value ->
                    profileSetupViewModel.onMetricChanged(type, value)
                },
                onBackClick = { profileSetupViewModel.backToPreviousStep() }
            )

            ProfileSetupStep.FITNESS_GOAL -> GoalScreen(
                profileRegistrationUiState = uiState,
                onGoalSelected = { profileSetupViewModel.selectedGoal(fitnessGoal = it) },
                onContinue = { profileSetupViewModel.proceedToLevelStep() },
                onBack = { profileSetupViewModel.backToPreviousStep() }
            )

            ProfileSetupStep.FITNESS_LEVEL -> FitnessLevelScreen(
                state = uiState,
                onLevelSelected = { profileSetupViewModel.onFitnessLevelSelection(fitnessLevel = it) },
                onContinue = { profileSetupViewModel.proceedToDaySelectionStep() },
                onBack = { profileSetupViewModel.backToPreviousStep() }
            )

            ProfileSetupStep.WEEKLY_SCHEDULE -> WeekSetupScreen(
                state = uiState,
                onContinue = {profileSetupViewModel.createProfile()},
                onSelect = { profileSetupViewModel.onWeekDaySelected(it) },
                onBack = { profileSetupViewModel.backToPreviousStep() }
            )

            ProfileSetupStep.COMPLETED -> TODO()
        }
    }
}

@Composable
fun ProfileSetupScreen(
    uiState: ProfileRegistrationUiState,
    onMetricChange: (Validator.ValidationType, String) -> Unit,
    onContinueClick: () -> Unit,
    onGenderChange: (Gender) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            StepIndicator(
                currentStep = uiState.currentStep,
                stepCount = uiState.totalSteps,
                title = "Set up your profile",
                onBack = {}
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Profile illustration
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF60A5FA),
                                Color(0xFF8B5CF6)
                            )
                        ),
                        shape = RoundedCornerShape(40.dp)
                    )
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile_illustration),
                    contentDescription = "Profile illustration",
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title and description
            Text(
                text = "Let's get to know you",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We'll use this information to personalize your experience",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Your Name field with icon
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Your Name",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Box {
                    OutlinedTextField(
                        value = uiState.metricsUiState.name,
                        onValueChange = { onMetricChange(Validator.ValidationType.NAME, it) },
                        placeholder = {
                            Text(
                                text = "John Doe",
                                color = Color(0xFF9CA3AF),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6), // focus:border-blue-500
                            unfocusedBorderColor = Color(0xFFE5E7EB), // border-gray-200
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = Color(0xFF3B82F6)
                        ),
                        shape = RoundedCornerShape(8.dp), // rounded-lg
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        singleLine = true,
                        isError = uiState.metricsUiState.hasNameError,
                        supportingText = if (uiState.metricsUiState.hasError && uiState.metricsUiState.nameError != null) {
                            {
                                Text(
                                    text = uiState.metricsUiState.nameError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        } else null,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.user_icon), // New user icon SVG
                                contentDescription = "Name",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFF3B82F6) // text-blue-500
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Gender",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Male option
                    val isMaleSelected = uiState.gender == Gender.MALE
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (isMaleSelected) Color(0xFFEFF6FF) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = if (isMaleSelected) 2.dp else 1.dp,
                                color = if (isMaleSelected) Color(0xFF2563EB) else Color(0xFFE5E7EB),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .clickable {
                                onGenderChange(Gender.MALE)
                            }
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = if (isMaleSelected) Color(0xFF2563EB) else Color(
                                            0xFFDBEAFE
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = if (isMaleSelected) Color(0xFF2563EB) else Color(
                                            0xFFDBEAFE
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .clickable {
                                        onGenderChange(Gender.MALE)
                                    }
                            )
                            Text(
                                text = "Male",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = if (isMaleSelected) Color(0xFF2563EB) else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    // Female option
                    val isFemaleSelected = uiState.gender == Gender.FEMALE
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (isFemaleSelected) Color(0xFFFAF5FF) else Color.White,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = if (isFemaleSelected) Color(0xFF9333EA) else Color(
                                    0xFFE5E7EB
                                ),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .clickable {
                                onGenderChange(Gender.FEMALE)
                            }
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = if (isFemaleSelected) Color(0xFF9333EA) else Color(
                                            0xFFF3E8FF
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = if (isFemaleSelected) Color(0xFF9333EA) else Color(
                                            0xFFF3E8FF
                                        ),
                                        shape = RoundedCornerShape(18.dp)
                                    )
                            )
                            Text(
                                text = "Female",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = if (isFemaleSelected) Color(0xFF9333EA) else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Age field with icon
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Your Age",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    value = uiState.metricsUiState.age,
                    onValueChange = { onMetricChange(Validator.ValidationType.AGE, it) },
                    placeholder = {
                        Text(
                            text = "25",
                            color = Color(0xFF9CA3AF),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8B5CF6), // focus:border-purple-500
                        unfocusedBorderColor = Color(0xFFE5E7EB), // border-gray-200
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        cursorColor = Color(0xFF8B5CF6)
                    ),
                    shape = RoundedCornerShape(8.dp), // rounded-lg
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    isError = uiState.metricsUiState.hasAgeError,
                    supportingText = if (uiState.metricsUiState.hasError && uiState.metricsUiState.ageError != null) {
                        {
                            Text(
                                text = uiState.metricsUiState.ageError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else null,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ageicon), // Age icon
                            contentDescription = "Age",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF8B5CF6) // text-purple-500
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            CustomButton(onContinueClick = onContinueClick)

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(
        uiState = ProfileRegistrationUiState(),
        onContinueClick = {},
        onGenderChange = {},
        onMetricChange = { _, _ -> }
    )
}


@Preview(showBackground = true)
@Composable
fun ProfileSetupScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        ProfileSetupScreen(
            uiState = ProfileRegistrationUiState(),
            onContinueClick = {},
            onGenderChange = {},
            onMetricChange = { _, _ -> }
        )
    }
}
