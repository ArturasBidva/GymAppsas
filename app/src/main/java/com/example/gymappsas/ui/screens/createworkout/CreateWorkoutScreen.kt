package com.example.gymappsas.ui.screens.createworkout

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymappsas.ui.reusable.LoadingCircle
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.ui.screens.createexercise.InputField

@Composable
fun CreateWorkoutScreen(
    createWorkoutViewModel: CreateWorkoutViewModel,
    onBackClick: () -> Unit,
    onSelectedCategory: (List<String>) -> Unit,
    navigateToAddExerciseToWorkoutFragment: () -> Unit,
    onWorkoutTitleChange: (String) -> Unit,
    onWorkoutDescriptionChange: (String) -> Unit
) {
    val uiState by createWorkoutViewModel.uiState.collectAsState()

    if (!uiState.isLoading) {
        Content(
            onBackClick = { onBackClick() },
            uiState = uiState,
            onSelectedCategory = onSelectedCategory,
            navigateToAddExerciseToWorkoutFragment = navigateToAddExerciseToWorkoutFragment,
            onWorkoutTitleChange = { onWorkoutTitleChange(it) },
            onWorkoutDescriptionChange = { onWorkoutDescriptionChange(it) }
        )
    } else {
        LoadingCircle()
    }
}

@Composable
private fun Content(
    onBackClick: () -> Unit,
    uiState: CreateWorkoutUiState,
    onSelectedCategory: (List<String>) -> Unit,
    navigateToAddExerciseToWorkoutFragment: () -> Unit,
    onWorkoutTitleChange: (String) -> Unit,
    onWorkoutDescriptionChange: (String) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Title",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = lexendRegular,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            InputField(
                modifier = Modifier.padding(horizontal = 16.dp),
                input = uiState.workoutTitle,
                onInputChange = { onWorkoutTitleChange(it) },
                emptyFieldSting = "Workout title",
                hasErrors = uiState.hasTitleError,
                errorMessage = uiState.titleErrorMessage
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Description",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = lexendRegular,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            InputField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(144.dp),
                input = uiState.workoutDescription,
                onInputChange = { onWorkoutDescriptionChange(it) },
                emptyFieldSting = "Workout description",
                hasErrors = uiState.hasDescriptionError,
                errorMessage = uiState.descriptionErrorMessage
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Select categories for workout",
                fontSize = 18.sp,
                fontFamily = lexenBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CheckboxMinimalExample(
                exerciseCategories = uiState.exerciseCategories.map { it -> it.name.replaceFirstChar { it.uppercaseChar() } },
                onSelectedCategoriesChanged = { onSelectedCategory(it) })
            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0A6BD9))
                    .clickable { navigateToAddExerciseToWorkoutFragment() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Continue",
                    fontFamily = lexenBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFFF7FAFC)
                )
            }

        }
    }
}


@Preview
@Composable
fun ContentPrev() {
    Content(
        onBackClick = {},
        uiState = CreateWorkoutUiState(),
        onSelectedCategory = {},
        navigateToAddExerciseToWorkoutFragment = {},
        onWorkoutDescriptionChange = {},
        onWorkoutTitleChange = {}
    )
}

@Composable
fun CheckboxMinimalExample(
    exerciseCategories: List<String>,
    onSelectedCategoriesChanged: (List<String>) -> Unit
) {
    val selectedCategories = remember { mutableStateListOf<String>() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val halfSize = (exerciseCategories.size + 1) / 2
        val firstColumnItems = exerciseCategories.take(halfSize)
        val secondColumnItems = exerciseCategories.drop(halfSize)

        Column(modifier = Modifier.weight(1f)) {
            firstColumnItems.forEach { exerciseCategory ->
                val isChecked = selectedCategories.contains(exerciseCategory)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedCategories.add(exerciseCategory)
                            } else {
                                selectedCategories.remove(exerciseCategory)
                            }
                            onSelectedCategoriesChanged(selectedCategories.toList())
                        }
                    )
                    Text(
                        text = exerciseCategory,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = lexendRegular
                    )
                }
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            secondColumnItems.forEach { exerciseCategory ->
                val isChecked = selectedCategories.contains(exerciseCategory)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedCategories.add(exerciseCategory)
                            } else {
                                selectedCategories.remove(exerciseCategory)
                            }
                            onSelectedCategoriesChanged(selectedCategories.toList())
                        }
                    )
                    Text(
                        text = exerciseCategory,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = lexendRegular
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckBoxPreview() {
    CheckboxMinimalExample(exerciseCategories = listOf(), {})
}


