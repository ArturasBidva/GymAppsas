package com.example.gymappsas.ui.screens.profilesetup

import CustomButton
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun MetricsScreen(
    state: ProfileRegistrationUiState,
    onMetricChange: (Validator.ValidationType, String) -> Unit,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val bmi = state.metricsUiState.bmi?.toFloatOrNull()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            StepIndicator(
                stepCount = state.totalSteps,
                currentStep = state.currentStep,
                title = "Set your metrics",
                onBack = onBackClick)

            Spacer(modifier = Modifier.height(20.dp))

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
                    painter = painterResource(id = R.drawable.body_measurements_illustration),
                    contentDescription = "Body measurements",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Your body measurements",
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
                text = "This helps us track your progress over time",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Height (cm)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                OutlinedTextField(
                    value = state.metricsUiState.height,
                    onValueChange = { onMetricChange(Validator.ValidationType.HEIGHT, it) },
                    placeholder = {
                        Text(
                            text = "Enter your height",
                            color = Color(0xFF9CA3AF),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF8F9FA),
                        unfocusedContainerColor = Color(0xFFFFFFFF),
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.height),
                            contentDescription = "Height icon",
                            tint = Color(0xFF59D05E)
                        )
                    },
                    isError = state.metricsUiState.hasHeightError,
                    supportingText = if (state.metricsUiState.hasError && state.metricsUiState.heightError != null) {
                        {
                            Text(
                                text = state.metricsUiState.heightError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else null,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Weight (kg)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                OutlinedTextField(
                    value = state.metricsUiState.weight,
                    onValueChange = { onMetricChange(Validator.ValidationType.WEIGHT, it) },
                    placeholder = {
                        Text(
                            text = "Enter your weight",
                            color = Color(0xFF9CA3AF),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF8F9FA),
                        unfocusedContainerColor = Color(0xFFFFFFFF),
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.weight),
                            contentDescription = "Height icon",
                            tint = Color(0xFFFF5722)
                        )
                    },
                    isError = state.metricsUiState.hasWeightError,
                    supportingText = if (state.metricsUiState.hasError && state.metricsUiState.weightError != null) {
                        {
                            Text(
                                text = state.metricsUiState.weightError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    } else null,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BMI Section card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp), // rounded-xl
                elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 2.dp) // shadow-sm
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFEFF6FF), // from-blue-50
                                    Color(0xFFF0FDF4)  // to-green-50
                                )
                            ),
                            shape = RoundedCornerShape(16.dp) // rounded-xl
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFDBEAFE), // border-blue-100
                            shape = RoundedCornerShape(16.dp) // rounded-xl
                        )
                        .padding(20.dp) // p-5 equivalent
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart),
                                contentDescription = "Heart",
                                modifier = Modifier.size(20.sp.value.dp),
                                tint = Color(0xFFEF4444) // text-red-500
                            )
                            Text(
                                text = "Your BMI",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium // font-medium
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 14.sp
                            )
                        }


                        if (bmi != null) {
                            Text(
                                text = String.format("%.1f", bmi),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold, // font-bold
                                    fontSize = 20.sp // text-xl equivalent
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = getBMICategory(bmi),
                                    style = MaterialTheme.typography.bodySmall, // text-sm
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                var showBMIDialog by remember { mutableStateOf(false) }

                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(
                                            color = Color(0xFF6B7280),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { showBMIDialog = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "?",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        color = Color.White
                                    )
                                }

                                if (showBMIDialog) {
                                    BMIInfoDialog(
                                        onDismiss = { showBMIDialog = false }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // BMI Progress Bar (h-2 bg-gray-200 rounded-full)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp) // h-2 equivalent
                                    .background(
                                        color = Color(0xFFE5E7EB), // bg-gray-200
                                        shape = RoundedCornerShape(4.dp) // rounded-full equivalent
                                    )
                            ) {
                                val progress = minOf(100f, (bmi / 40f) * 100f) / 100f
                                val bmiColor = when {
                                    bmi < 18.5f -> Color(0xFFFBBF24) // yellow
                                    bmi < 25f -> Color(0xFF10B981)   // green
                                    bmi < 30f -> Color(0xFFF97316)   // orange
                                    else -> Color(0xFFEF4444)        // red
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(progress)
                                        .background(
                                            color = bmiColor,
                                            shape = RoundedCornerShape(4.dp) // rounded-full equivalent
                                        )
                                )
                            }
                        } else {
                            Text(
                                text = "Enter your height and weight to calculate",
                                style = MaterialTheme.typography.bodySmall, // text-sm
                                color = Color(0xFF6B7280), // text-gray-600
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            CustomButton(onContinueClick = onContinueClick)
        }
    }
}

@Composable
fun BMIInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("What is BMI?") },
        text = {
            Text(
                text = "BMI (Body Mass Index) is a measure that uses your height and weight to work out if your weight is healthy.\n\n" +
                        "Formula: BMI = weight (kg) ÷ height² (m²)\n\n" +
                        "Categories:\n" +
                        "• Under 18.5: Underweight\n" +
                        "• 18.5-24.9: Normal weight\n" +
                        "• 25-29.9: Overweight\n" +
                        "• 30+: Obese"
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MetricsScreenLightPreview() {
    AppTheme(darkTheme = false) {
        MetricsScreen(
            state = ProfileRegistrationUiState(currentStep = 2),
            onContinueClick = {},
            onMetricChange = { _, _ -> },
            onBackClick = {}
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
            onMetricChange = { _, _ -> },
            onBackClick = {}
        )
    }
}