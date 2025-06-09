package com.example.gymappsas.ui.screens.addexercisetoworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.gymappsas.data.db.models.exercises.Exercise
import com.example.gymappsas.data.db.models.exerciseworkouts.ExerciseWorkout
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.ui.screens.createworkout.CreateWorkoutViewModel

class AddExerciseToWorkoutFragment : Fragment() {
    private val addExerciseToWorkoutViewModel: AddExerciseToWorkoutViewModel by activityViewModels()
    private val createWorkoutViewModel: CreateWorkoutViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                    val createWorkoutState by createWorkoutViewModel.uiState.collectAsState()
                if (createWorkoutState.createdWorkoutId != null && !createWorkoutState.hasNavigated) {
                    val workoutId = createWorkoutState.createdWorkoutId!!
                    LaunchedEffect(workoutId) {
                        createWorkoutViewModel.setNavigated()
                        val action = AddExerciseToWorkoutFragmentDirections
                            .actionAddExerciseToWorkoutFragmentToWorkoutCreatedFragment(workoutId = workoutId)
                        findNavController().navigate(action)
                    }
                }
                AddExerciseToWorkout(viewModel = addExerciseToWorkoutViewModel,
                    createWorkoutViewModel = createWorkoutViewModel,
                    onNavigateBack = { findNavController().popBackStack() },
                    onSelectedExerciseChange = {
                        addExerciseToWorkoutViewModel.toggleExerciseSelection(
                            it
                        )
                    },
                    navigateToExerciseDetails = {
                        val action =
                            AddExerciseToWorkoutFragmentDirections.actionAddExerciseToWorkoutFragmentToExerciseDetailsFragment(
                                exerciseId = it
                            )
                        findNavController().navigate(action)
                    },
                    onSaveWorkoutClick = {
                        val workoutTitle = createWorkoutState.workoutTitle
                        val workoutDescription = createWorkoutState.workoutDescription
                        val selectedExercises = addExerciseToWorkoutViewModel.uiState.value.selectedExercises

                        val exerciseWorkouts = selectedExercises.map { it.toExerciseWorkout() }
                        val workout = Workout(
                            title = workoutTitle,
                            description = workoutDescription,
                            exerciseWorkouts = exerciseWorkouts
                        )
                        createWorkoutViewModel.createWorkout(workout)
                    })
            }
        }
    }

    private fun Exercise.toExerciseWorkout(): ExerciseWorkout {
        return ExerciseWorkout(exercise = this)
    }
}