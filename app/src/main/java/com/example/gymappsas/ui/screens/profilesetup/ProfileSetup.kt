package com.example.gymappsas.ui.screens.profilesetup

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gymappsas.R
import com.example.gymappsas.ui.AppTheme
import com.example.gymappsas.ui.screens.createexercise.InputField
import com.example.gymappsas.ui.screens.workoutprep.Picker
import com.example.gymappsas.ui.screens.workoutprep.PickerState
import com.example.gymappsas.ui.screens.workoutprep.rememberPickerState
import com.example.gymappsas.util.PickerType
import com.example.gymappsas.util.Validator

@Composable
fun ProfileSetup(profileSetupViewModel: ProfileSetupViewModel,navigate: () -> Unit) {
    val uiState by profileSetupViewModel.uiState.collectAsState()
    val pickerType = profileSetupViewModel.showPicker.value

    Crossfade(targetState = uiState.profileSetupStep, label = "cross fade") { screen ->
        when (screen) {
            ProfileSetupStep.NAME -> ProfileSetupScreen(
                uiState = uiState,
                onNameChange = { profileSetupViewModel.onNameChanged(it) },
                navigate = { profileSetupViewModel.proceedToGenderStep() }
            )

            ProfileSetupStep.GENDER -> GenderSelectionScreen(
                selectedGender = uiState.gender,
                onGenderSelected = { profileSetupViewModel.onGenderChanged(it) },
                onContinue = {profileSetupViewModel.proceedToMetricsStep()}
            )

            ProfileSetupStep.METRICS -> MetricsScreen(
                state = uiState,
                onContinueClick = {profileSetupViewModel.saveProfile() },
                onClickListener = { type ->
                    profileSetupViewModel.showPickerDialog(type)
                }
            )

            ProfileSetupStep.COMPLETED -> {
                navigate()
            }
        }
    }
    if (pickerType != PickerType.NONE) {
        PickerDialog(
            viewModel = profileSetupViewModel,
            pickerType = pickerType
        )
    }
}

@Composable
fun ProfileSetupScreen(
    uiState: ProfileRegistrationUiState,
    onNameChange: (String) -> Unit,
    navigate: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile_circle_svgrepo_com_1),
                    contentDescription = "Profile Setup",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )

                Text(
                    text = "Hello, first introduce yourself",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Let's create your personalized fitness profile",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Input Section
            InputField(
                modifier = Modifier.fillMaxWidth(),
                input = uiState.name,
                onInputChange = { onNameChange(it) },
                emptyFieldSting = "Enter your name...",
                hasErrors = uiState.hasNameError,
                errorMessage = uiState.nameError,
                icon = Icons.Default.Person,
                label = "Full Name" // Add label parameter to your InputField
            )

            // Continue Button
            FilledTonalButton(
                onClick = navigate,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Preview
@Composable
private fun ProfileSetupScreenPreview() {
    ProfileSetupScreen(
        uiState = ProfileRegistrationUiState(),
        onNameChange = {},
        navigate = {}
    )
}

@Composable
fun MetricsInputField(
    title: String,
    unit: String,
    value: String,
    errorMessage: String?,
    showError: Boolean,
    onClickListener: () -> Unit,
    icon: Int? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(onClick = onClickListener)
                .border(
                    width = 1.dp,
                    color = if (showError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icon and value row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    icon?.let {
                        Icon(
                            painter = painterResource(id = it),
                            contentDescription = "Input field icon", // Never null!
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(24.dp)
                        )
                    }
                    Text(
                        text = value.ifEmpty { "Select $title" },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (value.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (showError && errorMessage != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Composable
fun MetricsScreen(
    state: ProfileRegistrationUiState,
    onContinueClick: () -> Unit,
    onClickListener: (PickerType) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Profile Information",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(Modifier.height(16.dp))

            MetricsInputField(
                title = "Age",
                unit = "years",
                value = state.metricsUiState.age,
                errorMessage = state.metricsUiState.ageError,
                showError = state.metricsUiState.hasError,
                onClickListener = { onClickListener(PickerType.AGE) },
                icon = R.drawable.ageicon
            )

            MetricsInputField(
                title = "Weight",
                unit = "kg",
                value = state.metricsUiState.weight,
                errorMessage = state.metricsUiState.weightError,
                showError = state.metricsUiState.hasError,
                onClickListener = { onClickListener(PickerType.WEIGHT) },
                icon = R.drawable.weight
            )

            MetricsInputField(
                title = "Height",
                unit = "cm",
                value = state.metricsUiState.height,
                errorMessage = state.metricsUiState.heightError,
                showError = state.metricsUiState.hasError,
                onClickListener = { onClickListener(PickerType.HEIGHT) },
                icon = R.drawable.height
            )

            Spacer(Modifier.weight(1f))

            FilledTonalButton(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(
                    text = "Complete Profile",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}


@Composable
fun MetricsPicker(
    state: PickerState,
    items: List<String>,
    visibleItemsCount: Int,
    onCancelClick: () -> Unit,
    title: String,
    metric: String,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onCancelClick) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 6.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Picker(
                    state = state,
                    items = items,
                    visibleItemsCount = visibleItemsCount,
                    modifier = modifier,
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Text(
                    text = "${state.selectedItem} $metric",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onCancelClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("Cancel")
                    }

                    Spacer(Modifier.width(16.dp))

                    Button(
                        onClick = { onConfirm(state.selectedItem) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
private fun PickerDialog(
    viewModel: ProfileSetupViewModel,
    pickerType: PickerType
) {
    val options = viewModel.getPickerOptions(pickerType)
    val title = when (pickerType) {
        PickerType.AGE -> "Select Age"
        PickerType.WEIGHT -> "Select Weight"
        PickerType.HEIGHT -> "Select Height"
        else -> ""
    }
    val metric = when (pickerType) {
        PickerType.AGE -> "years"
        PickerType.WEIGHT -> "kg"
        PickerType.HEIGHT -> "cm"
        else -> ""
    }
    val pickerState = rememberPickerState(
        initialIndex = 0,
        items = options
    )


    MetricsPicker(
        state = pickerState,
        items = options,
        visibleItemsCount = 5,
        onCancelClick = { viewModel.hidePickerDialog() },
        title = title,
        metric = metric,
        onConfirm = { selectedValue ->
            viewModel.onMetricChanged(
                when (pickerType) {
                    PickerType.AGE -> Validator.ValidationType.AGE
                    PickerType.WEIGHT -> Validator.ValidationType.WEIGHT
                    PickerType.HEIGHT -> Validator.ValidationType.HEIGHT
                    else -> throw IllegalArgumentException("Invalid picker type")
                },
                selectedValue
            )
            viewModel.hidePickerDialog()
        }

    )
}


@Preview(showBackground = true)
@Composable
fun MetricsPickerLightPreview() {
    AppTheme(darkTheme = false) {
        MetricsPicker(
            state = rememberPickerState(),
            items = (0..100).map { it.toString() },
            visibleItemsCount = 5,
            onCancelClick = {},
            title = "Select Weight",
            metric = "kg",
            onConfirm = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MetricsPickerDarkPreview() {
    AppTheme(darkTheme = true) {
        MetricsPicker(
            state = rememberPickerState(),
            items = (0..100).map { it.toString() },
            visibleItemsCount = 5,
            onCancelClick = {},
            title = "Select Weight",
            metric = "kg",
            onConfirm = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MetricsScreenLightPreview() {
    AppTheme(darkTheme = false) {
        MetricsScreen(
            state = ProfileRegistrationUiState(),
            onContinueClick = {},
            onClickListener = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MetricsScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        MetricsScreen(
            state = ProfileRegistrationUiState(),
            onContinueClick = {},
            onClickListener = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSetupScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        ProfileSetupScreen(
            uiState = ProfileRegistrationUiState(),
            onNameChange = {},
            navigate = {}
        )
    }
}