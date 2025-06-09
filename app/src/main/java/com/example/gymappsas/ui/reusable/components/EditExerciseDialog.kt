package com.example.gymappsas.ui.reusable.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.util.MockExerciseWorkoutData

@Composable
fun EditableExerciseDialog(
    exerciseWorkout: ExerciseWorkout,
    onDismiss: () -> Unit,
    onSave: (ExerciseWorkout) -> Unit
) {

}

@Preview
@Composable
private fun EditableExerciseDialogPreview() {
    EditableExerciseDialog(
        exerciseWorkout = MockExerciseWorkoutData.mockExerciseWorkouts[0],
        onDismiss = {}) {

    }
}