import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.gymappsas.R
import android.widget.Toast
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.ui.reusable.lexenBold
import com.example.gymappsas.ui.reusable.lexendMedium
import com.example.gymappsas.ui.reusable.lexendRegular
import com.example.gymappsas.ui.screens.workoutprep.RestTimeOption
import com.example.gymappsas.ui.screens.workoutprep.TrainingMethod
import com.example.gymappsas.ui.screens.workoutprep.WorkoutPreparationUiState
import com.example.gymappsas.ui.screens.workoutprep.WorkoutPreparationViewModel
import com.example.gymappsas.util.GetImagePath
import com.example.gymappsas.util.MockExerciseWorkoutData
import com.example.gymappsas.util.MockWorkoutData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrepareWorkoutScreen(
    onStart: (Workout) -> Unit,
    onBack: () -> Unit,
    viewModel: WorkoutPreparationViewModel = hiltViewModel(),
    onSelectTrainingMethod: (TrainingMethod) -> Unit,
    onUpdate: (ExerciseWorkout) -> Unit,
    handleNextStep: () -> Unit,
    isNextDisabled: () -> Boolean,
    onRestTimeChanged: (Int) -> Unit,
    onEditWeights: () -> Unit,
    onCustomRestTimeChange: (String) -> Unit,
    onSaveWorkout: () -> Unit = {},
    onNameChange: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CustomTopBar("Prepare Workout", onBack = onBack)
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            if (uiState.selectedWorkout == null) {
                Text("Loading workout...")
            } else {
                Content(
                    uiState = uiState,
                    onSelectedTrainingMethodChanged = { onSelectTrainingMethod(it) },
                    onRestTimeChanged = { onRestTimeChanged(it) },
                    onWorkoutUpdate = { onUpdate(it) },
                    onEditWeights = onEditWeights,
                    onNextStep = {
                        if (uiState.currentStep < 4) handleNextStep()
                        else uiState.selectedWorkout?.let { onStart(it) }
                    },
                    isNextDisabled = isNextDisabled(),
                    onCustomRestTimeChange = { onCustomRestTimeChange(it) },
                    saveWorkout = {
                        if (!uiState.workoutSaved && uiState.workoutVariantName.isNotBlank()) {
                            onSaveWorkout()
                            Toast.makeText(context, "Workout variant saved!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    isSaved = uiState.workoutSaved,
                    onNameChange = onNameChange,
                    isSaveDisabled = uiState.workoutVariantName.isBlank()
                )
            }
        }
    }
}

@Composable
private fun Content(
    uiState: WorkoutPreparationUiState,
    onSelectedTrainingMethodChanged: (TrainingMethod) -> Unit,
    onRestTimeChanged: (Int) -> Unit,
    onWorkoutUpdate: (ExerciseWorkout) -> Unit,
    onEditWeights: () -> Unit,
    onNextStep: () -> Unit,
    isNextDisabled: Boolean,
    onCustomRestTimeChange: (String) -> Unit,
    saveWorkout: () -> Unit,
    modifier: Modifier = Modifier,
    isSaved: Boolean = false,
    onNameChange: (String) -> Unit = {},
    isSaveDisabled: Boolean = false
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        StepIndicator(currentStep = uiState.currentStep)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState.currentStep) {
                1 -> MethodSelectionStep(
                    trainingMethods = uiState.trainingMethods,
                    selectedMethod = uiState.selectedMethod,
                    onSelect = { onSelectedTrainingMethodChanged(it) }
                )

                2 -> RestTimeSelectionStep(
                    restTimeOptions = uiState.restTimeOptions,
                    selectedRestTime = uiState.restTime,
                    customRestTime = uiState.customRestTime,
                    onCustomRestTimeChange = { onCustomRestTimeChange(it) },
                    onTimeChanged = { onRestTimeChanged(it) }
                )

                3 -> WeightSelectionStep(
                    exercises = uiState.selectedWorkout?.exerciseWorkouts ?: emptyList(),
                    selectedMethod = uiState.selectedMethod,
                    trainingMethods = uiState.trainingMethods,
                    onUpdate = { onWorkoutUpdate(it) }
                )

                4 -> ConfirmationStep(
                    exercises = uiState.selectedWorkout?.exerciseWorkouts ?: emptyList(),
                    selectedMethod = uiState.selectedMethod,
                    restTime = uiState.restTime,
                    trainingMethods = uiState.trainingMethods,
                    onEditWeights = { onEditWeights() },
                    saveWorkout = saveWorkout,
                    isSaved = isSaved,
                    workoutVariantName = uiState.workoutVariantName,
                    onNameChange = onNameChange
                )
            }
        }

        if (uiState.currentStep == 4) {
            // Two buttons for confirmation step
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!isSaved) {
                    Button(
                        onClick = saveWorkout,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSaveDisabled) Color(0xFFF3F4F6) else Color(
                                0xFF16A34A
                            )
                        ),
                        enabled = !isSaveDisabled
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.save_icon),
                            contentDescription = "Save",
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFFF7FAFC)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Variant",
                            color = if (isSaveDisabled) Color.Gray else Color.White,
                            fontSize = 16.sp,
                            fontFamily = lexendMedium
                        )
                    }
                }
                Button(
                    onClick = onNextStep,
                    modifier = Modifier
                        .let { modifier ->
                            if (isSaved) modifier.fillMaxWidth() else modifier.weight(1f)
                        }
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    ),
                    enabled = !isNextDisabled
                ) {
                    Icon(
                        painter = painterResource(R.drawable.start_icon),
                        contentDescription = "Start",
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start Workout",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = lexendMedium
                    )
                }
            }
        } else {
            // Single continue button for other steps
            Button(
                onClick = onNextStep,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        isNextDisabled -> Color(0xFFF3F4F6)
                        else -> Color(0xFF3B82F6)
                    }
                ),
                enabled = !isNextDisabled
            ) {
                Text(
                    text = "Continue",
                    color = when {
                        isNextDisabled -> Color.Gray
                        else -> Color.White
                    },
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun StepIndicator(currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (step in 1..4) {
            val isDone = currentStep > step
            val isActive = currentStep == step
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isActive -> Color(0xFF3B82F6)
                            isDone -> Color.White
                            else -> Color(0xFFF3F4F6)
                        }
                    )
                    .border(
                        width = 2.dp,
                        color = when {
                            isActive -> Color(0xFF3B82F6)
                            isDone -> Color(0xFF3B82F6).copy(alpha = 0.5f)
                            else -> Color(0xFFF3F4F6)
                        },
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$step",
                    color = when {
                        isActive -> Color.White
                        isDone -> Color(0xFF3B82F6)
                        else -> Color.Gray
                    },
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 18.sp
                )
            }

            if (step < 4) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(3.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(
                                    if (currentStep > step) Color(0xFF3B82F6) else Color(0xFFF3F4F6),
                                    if (currentStep > step) Color(0xFF3B82F6).copy(alpha = 0.35f) else Color(
                                        0xFFF3F4F6
                                    )
                                )
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun MethodSelectionStep(
    trainingMethods: List<TrainingMethod>,
    selectedMethod: TrainingMethod?,
    onSelect: (TrainingMethod) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Choose your workout intensity style",
            fontFamily = lexenBold,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            "How would you like to train today?",
            fontFamily = lexendRegular,
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            trainingMethods.forEach { method ->
                MethodCard(
                    method = method,
                    isSelected = selectedMethod == method,
                    onSelect = onSelect
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        selectedMethod?.let { method ->
            MethodInfoCard(method = method)
        }
    }
}

@Composable
fun MethodInfoCard(method: TrainingMethod) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = Color(0xFF3B82F6),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    "About ${method.displayName}",
                    fontFamily = lexendMedium,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Text(
                    method.description,
                    fontFamily = lexendRegular,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun RestTimeSelectionStep(
    restTimeOptions: List<RestTimeOption>,
    selectedRestTime: Int,
    customRestTime: String,
    onCustomRestTimeChange: (String) -> Unit,
    onTimeChanged: (Int) -> Unit,
) {
    Column {
        Text(
            "Select Rest Interval",
            fontFamily = lexenBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            "How long do you want to rest between sets?",
            fontFamily = lexendRegular,
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            restTimeOptions.forEach { option ->
                val isSelected = if (option.value == -1) {
                    selectedRestTime == -1
                } else {
                    selectedRestTime == option.value
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) Color(0xFFDBEAFE) else Color.White
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color(0xFF3B82F6) else Color(0xFFF3F4F6),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            if (option.value == -1) {
                                onTimeChanged(-1)
                            } else {
                                onTimeChanged(option.value)
                                onCustomRestTimeChange("")
                            }
                        }
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (isSelected) Color(0xFF3B82F6) else Color.Transparent
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color.Transparent else Color(0xFFF3F4F6),
                                    shape = RoundedCornerShape(50)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                option.label,
                                fontFamily = lexendMedium,
                                fontSize = 16.sp,
                                color = if (isSelected) Color(0xFF3B82F6) else Color.Black
                            )
                            Text(
                                option.description,
                                fontFamily = lexendRegular,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )

                            if (option.value == -1) {
                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = customRestTime,
                                        onValueChange = { newValue ->
                                            val filtered = newValue.filter { it.isDigit() }
                                            onCustomRestTimeChange(filtered)
                                            onTimeChanged(-1)

                                        },
                                        modifier = Modifier.width(100.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        visualTransformation = NumberTransformation(),
                                        placeholder = { Text("0") }
                                    )
                                    Text(
                                        "seconds",
                                        fontFamily = lexendRegular,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Info box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        "Rest Time Tip",
                        fontFamily = lexendMedium,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        when {
                            selectedRestTime <= 60 -> stringResource(R.string.rest_time_60)
                            selectedRestTime <= 90 -> stringResource(R.string.rest_time_90)
                            else -> stringResource(R.string.rest_time_120)
                        },
                        fontFamily = lexendRegular,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun WeightSelectionStep(
    exercises: List<ExerciseWorkout>,
    selectedMethod: TrainingMethod?,
    trainingMethods: List<TrainingMethod>,
    onUpdate: (ExerciseWorkout) -> Unit
) {
    Column {
        Text(
            "Set the weight for each exercise",
            fontFamily = lexenBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        selectedMethod?.let { method ->
            Text(
                "Adjust based on your ${method.displayName.lowercase()} workout style",
                fontFamily = lexendRegular,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(exercises) { exercise ->
                ExerciseCard(
                    exerciseWorkout = exercise,
                    selectedMethod = selectedMethod,
                    trainingMethods = trainingMethods,
                    onUpdate = { onUpdate(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exerciseWorkout: ExerciseWorkout,
    selectedMethod: TrainingMethod?,
    trainingMethods: List<TrainingMethod>,
    onUpdate: (ExerciseWorkout) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = GetImagePath.getExerciseImagePath(
                        category = exerciseWorkout.exercise.primaryMuscles.first(),
                        exerciseName = exerciseWorkout.exercise.name
                    ),
                    contentDescription = exerciseWorkout.exercise.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        exerciseWorkout.exercise.name,
                        fontFamily = lexendMedium,
                        fontSize = 16.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.dumbell),
                                contentDescription = "Dumbbell Icon",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${exerciseWorkout.goal} sets",
                                fontFamily = lexendRegular,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }


                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.timer),
                                contentDescription = "Clock Icon",
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${exerciseWorkout.breakTime / 1000}s rest",
                                fontFamily = lexendRegular,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }


            selectedMethod?.let { method ->
                when (method) {
                    TrainingMethod.CONSTANT -> ConstantWeightInput(
                        exercise = exerciseWorkout,
                        onUpdate = { weight ->
                            val weights = List(exerciseWorkout.goal) { weight }
                            onUpdate(
                                exerciseWorkout.copy(
                                    weights = weights.map { it.toFloat() },
                                    maxWeight = weight.toFloat()
                                )
                            )
                        },
                        reps = method.reps
                    )

                    TrainingMethod.FORCE -> ForceWeightInput(
                        exercise = exerciseWorkout,
                        reps = method.reps,
                        onUpdate = { weight ->
                            val weights = List(exerciseWorkout.goal) { weight }
                            onUpdate(
                                exerciseWorkout.copy(
                                    weights = weights.map { it.toFloat() },
                                    maxWeight = weight.toFloat()
                                )
                            )
                        }
                    )

                    else -> PyramidWeightInput(
                        exercise = exerciseWorkout,
                        method = method,
                        onUpdate = { weight ->
                            val weights = calculateWeights(
                                maxWeight = weight.toFloat(),
                                sets = exerciseWorkout.goal,
                                method = method
                            )
                            onUpdate(
                                exerciseWorkout.copy(
                                    weights = weights.map { it.toFloat() },
                                    maxWeight = weight.toFloat()
                                )
                            )
                        },
                        reps = method.reps
                    )
                }
            }
        }
    }
}

@Composable
fun ConstantWeightInput(
    exercise: ExerciseWorkout,
    onUpdate: (Double) -> Unit,
    reps: List<Any>
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Weight for all sets (kg)",
            fontFamily = lexendMedium,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        WeightInputWithButtons(
            value = exercise.maxWeight.toDouble(),
            onValueChange = { onUpdate(it) },
            label = "Max Weight"
        )
        CustomSetBoxes(
            exercise = exercise,
            reps = reps
        )
    }
}

@Composable
fun PyramidWeightInput(
    exercise: ExerciseWorkout,
    method: TrainingMethod,
    onUpdate: (Double) -> Unit,
    reps: List<Any>
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            when (method) {
                TrainingMethod.PYRAMID -> "Starting weight (kg)"
                TrainingMethod.REVERSE_PYRAMID -> "Maximum weight (kg)"
                else -> "Maximum weight (kg)"
            },
            fontFamily = lexendMedium,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        WeightInputWithButtons(
            value = exercise.maxWeight.toDouble(),
            onValueChange = { onUpdate(it) },
            label = "Max Weight"
        )
        CustomSetBoxes(exercise = exercise, reps = reps)
    }
}

@Composable
fun ForceWeightInput(
    exercise: ExerciseWorkout,
    onUpdate: (Double) -> Unit,
    reps: List<Any>
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Working weight (kg)",
            fontFamily = lexendMedium,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        WeightInputWithButtons(
            value = exercise.maxWeight.toDouble(),
            onValueChange = { onUpdate(it) },
            label = "Max Weight"
        )
        CustomSetBoxes(
            exercise = exercise,
            reps = reps
        )
    }


}

@Composable
fun ConfirmationStep(
    exercises: List<ExerciseWorkout>,
    selectedMethod: TrainingMethod?,
    restTime: Int,
    trainingMethods: List<TrainingMethod>,
    onEditWeights: () -> Unit,
    saveWorkout: () -> Unit,
    isSaved: Boolean = false,
    workoutVariantName: String = "",
    onNameChange: (String) -> Unit = {}
) {
    Column {
        Text(
            "Your Workout Plan",
            fontFamily = lexenBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            "Review your customized workout before starting",
            fontFamily = lexendRegular,
            fontSize = 14.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Workout Name Input
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Workout Variant Name",
                    fontFamily = lexendMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = workoutVariantName,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "e.g., Heavy Push Day, Light Volume",
                            color = Color(0xFF9CA3AF)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 1
                )

                Text(
                    "Give this workout variant a name to save different weight configurations",
                    fontFamily = lexendRegular,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Workout details card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Workout Details",
                    fontFamily = lexendMedium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                DetailRow(
                    title = "Workout Style", value = selectedMethod?.displayName ?: ""
                )
                DetailRow(title = "Rest Time", value = "$restTime seconds")
                DetailRow(title = "Exercises", value = exercises.size.toString())
                DetailRow(
                    title = "Estimated Duration",
                    value = "${exercises.size * (exercises.firstOrNull()?.goal ?: 0) * (restTime / 60 + 0.5).toInt()} minutes"
                )
            }
        }

        // Exercise plan
        Text(
            "Exercise Plan",
            fontFamily = lexendMedium,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(exercises) { exercise ->
                ExerciseConfirmationCard(
                    exercise = exercise,
                    selectedMethod = selectedMethod,
                    trainingMethods = trainingMethods,
                    onEdit = onEditWeights
                )
            }
        }
    }
}

@Composable
fun DetailRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            fontFamily = lexendRegular,
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )
        Text(
            value,
            fontFamily = lexendMedium,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ExerciseConfirmationCard(
    exercise: ExerciseWorkout,
    selectedMethod: TrainingMethod?,
    trainingMethods: List<TrainingMethod>,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = GetImagePath.getExerciseImagePath(
                        category = exercise.exercise.primaryMuscles.first(),
                        exerciseName = exercise.exercise.name
                    ),
                    contentDescription = exercise.exercise.name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        exercise.exercise.name,
                        fontFamily = lexendMedium,
                        fontSize = 16.sp
                    )
                    Text(
                        "${exercise.goal} sets • ${exercise.breakTime / 1000}s rest",
                        fontFamily = lexendRegular,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Weight distribution
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                exercise.weights.forEachIndexed { index, weight ->
                    val reps = selectedMethod?.reps?.getOrNull(index)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Set ${index + 1}",
                                fontFamily = lexendRegular,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                "${weight}kg",
                                fontFamily = lexendMedium,
                                fontSize = 14.sp
                            )
                            Text(
                                if (reps != null) {
                                    if (reps is Int) "$reps reps" else reps.toString()
                                } else "",
                                fontFamily = lexendRegular,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

fun calculateWeights(maxWeight: Float, sets: Int, method: TrainingMethod): List<Float> {
    val percentages = when (method) {
        TrainingMethod.PYRAMID -> listOf(0.7f, 0.8f, 0.9f, 1.0f)
        TrainingMethod.FORCE -> listOf(0.85f, 0.85f, 0.85f, 0.85f)
        TrainingMethod.DROPSET -> listOf(1.0f, 0.9f, 0.8f, 0.7f)
        TrainingMethod.CONSTANT -> listOf(1.0f, 1.0f, 1.0f, 1.0f)
        TrainingMethod.REVERSE_PYRAMID -> listOf(1.0f, 0.9f, 0.8f, 0.7f)
    }

    val adjustedPercentages = if (sets > percentages.size) {
        percentages + List(sets - percentages.size) { percentages.last() }
    } else {
        percentages.take(sets)
    }

    return adjustedPercentages.map { percentage ->
        val weight = maxWeight * percentage
        (kotlin.math.round(weight / 2.5f) * 2.5f).coerceAtLeast(2.5f)
    }
}

// Preview composable
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PrepareWorkoutContentPreview() {
    val mockUiState = WorkoutPreparationUiState(
        selectedWorkout = MockWorkoutData.mockWorkout,
        currentStep = 4,
        trainingMethods = listOf(
            TrainingMethod.CONSTANT,
            TrainingMethod.PYRAMID
        ),
        selectedMethod = TrainingMethod.PYRAMID,
        restTime = 60,
        restTimeOptions = listOf(
            RestTimeOption(30, "30s", "Short rest"),
            RestTimeOption(60, "60s", "Standard rest")
        )
    )

    Content(
        uiState = mockUiState,
        onSelectedTrainingMethodChanged = {},
        onRestTimeChanged = {},
        onWorkoutUpdate = {},
        onEditWeights = {},
        onNextStep = {},
        isNextDisabled = false,
        onCustomRestTimeChange = {},
        saveWorkout = {},
        isSaveDisabled = false
    )
}

class NumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.filter { it.isDigit() }),
            offsetMapping = OffsetMapping.Identity
        )
    }
}

@Composable
fun WeightInputWithButtons(
    value: Double,
    onValueChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null
) {
    // Create state for TextFieldValue
    var textValue by remember(value) {
        mutableStateOf(
            TextFieldValue(
                text = DecimalFormatter.formatDouble(value),
                selection = TextRange(DecimalFormatter.formatDouble(value).length)
            )
        )
    }

    // Function to filter and validate decimal input
    fun filterDecimalInput(input: TextFieldValue, maxValue: Double = 300.0): TextFieldValue {
        var newText = input.text

        // Only allow digits and a single decimal point
        newText = newText.filter { it.isDigit() || it == '.' }

        // Ensure only one decimal point
        if (newText.count { it == '.' } > 1) {
            newText = newText.substring(0, newText.lastIndexOf('.'))
        }

        // Limit to 2 decimal places
        val parts = newText.split('.')
        if (parts.size > 1 && parts[1].length > 2) {
            newText = parts[0] + '.' + parts[1].substring(0, 2)
        }

        // Handle leading decimal point
        if (newText.startsWith('.')) {
            newText = "0$newText"
        }

        // Calculate new selection
        val selectionOffset = input.selection.start - (input.text.length - newText.length)
        val newSelection = TextRange(
            start = selectionOffset.coerceIn(0, newText.length),
            end = selectionOffset.coerceIn(0, newText.length)
        )
        val numericValue = newText.toDoubleOrNull()
        if (numericValue != null && numericValue > maxValue) {
            // Option A: Cap at maxValue
            // Format maxValue to avoid potential trailing ".0" if it's an integer
            newText = if (maxValue % 1 == 0.0) {
                maxValue.toInt().toString()
            } else {
                // Ensure maxValue itself is formatted to max 2 decimal places if needed
                // For 300, this won't be an issue.
                String.format("%.2f", maxValue).removeSuffix(".00").removeSuffix("0")
                    .removeSuffix(".")
            }
        }
        return input.copy(text = newText, selection = newSelection)

    }
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                fontFamily = lexendMedium,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color.White, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp)
                ) {
                    BasicTextField(
                        value = textValue,
                        onValueChange = { newValue ->
                            val filtered = filterDecimalInput(newValue)
                            textValue = filtered

                            // Convert to double and notify parent
                            val doubleValue = filtered.text.toDoubleOrNull() ?: 0.0
                            onValueChange(doubleValue)
                        },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontFamily = lexendMedium,
                            textAlign = TextAlign.Start
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 12.dp),
                    )
                    Text(
                        text = "kg",
                        fontFamily = lexendRegular,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            // Decrease Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
                    .clickable {
                        val newValue = (value - 2.5).coerceAtLeast(0.0)
                        onValueChange(newValue)
                        textValue = TextFieldValue(
                            text = DecimalFormatter.formatDouble(newValue),
                            selection = TextRange(DecimalFormatter.formatDouble(newValue).length)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "–",
                    fontSize = 20.sp,
                    fontFamily = lexendMedium,
                    color = Color(0xFF6B7280)
                )
            }

            Spacer(Modifier.width(8.dp))

            // Increase Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6FF))
                    .clickable {
                        val newValue = value + 2.5
                        onValueChange(newValue)
                        textValue = TextFieldValue(
                            text = DecimalFormatter.formatDouble(newValue),
                            selection = TextRange(DecimalFormatter.formatDouble(newValue).length)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontSize = 20.sp,
                    fontFamily = lexendMedium,
                    color = Color(0xFF3B82F6)
                )
            }
        }
    }
}

// Utility object for formatting decimal values
object DecimalFormatter {
    fun formatDouble(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            // Format to 2 decimal places but remove trailing zeros
            val formatted = "%.2f".format(value)
            formatted.removeSuffix("0").removeSuffix("0").removeSuffix(".")
        }
    }
}

@Preview
@Composable
private fun ExerciseDataPreview() {
    ExerciseCard(
        exerciseWorkout = MockExerciseWorkoutData.mockExerciseWorkouts.first(),
        selectedMethod = TrainingMethod.FORCE,
        trainingMethods = listOf(
            TrainingMethod.FORCE
        ),
        onUpdate = {},
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MethodCard(
    method: TrainingMethod,
    isSelected: Boolean,
    onSelect: (TrainingMethod) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF7FAFC)
            )
            .border(
                width = 2.dp,
                color = if (isSelected) Color(0xFF3B82F6) else Color(0xFFF3F4F6),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect(method) }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Color(0xFF3B82F6) else Color.Transparent
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Color.Transparent else Color(0xFFF3F4F6),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    method.displayName,
                    fontFamily = lexendMedium,
                    fontSize = 18.sp,
                    color = if (isSelected) Color(0xFF3B82F6) else Color.Black
                )
                Text(
                    method.description,
                    fontFamily = lexendRegular,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun CustomTopBar(title: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(Color(0xFFF7FAFC))
    ) {
        // Back button centered vertically
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onBack() }
            )
        }

        // Title centered both vertically and horizontally
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontFamily = lexenBold,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

//@Preview
//@Composable
//private fun PrepareWorkoutPreview() {
//    PrepareWorkoutScreen(
//        onStart = {},
//        onBack = {},
//        onSelectTrainingMethod = {},
//        onUpdate = {},
//        handleNextStep = {},
//        isNextDisabled = { false },
//        onRestTimeChanged = {},
//        onEditWeights = {},
//        onCustomRestTimeChange = {}
//    )
//}

@Composable
fun CustomSetBoxes(
    exercise: ExerciseWorkout,
    reps: List<Any>,
) {
    val weights = exercise.weights
    if ((weights.firstOrNull() ?: 0f) > 0f) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF3F4F6))
        ) {
            Text(
                text = "Working Sets",
                fontFamily = lexendMedium,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(weights.size) { index ->
                    val weight = weights[index]
                    val repValue = reps.getOrNull(index)
                    val repText = when (repValue) {
                        is Int -> "Reps: $repValue"
                        is String -> repValue
                        else -> "—"
                    }

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(110.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF7FAFC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "Set ${index + 1}",
                                fontFamily = lexendRegular,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = String.format("%.1f kg", weight),
                                fontFamily = lexendMedium,
                                fontSize = 14.sp,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = repText,
                                fontFamily = lexendRegular,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CustomSetBoxesPreview() {

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CustomSetBoxes(
                exercise = MockExerciseWorkoutData.mockExerciseWorkouts.first(),
                reps = listOf(10, 8, 8, 6)
            )
        }
    }
}