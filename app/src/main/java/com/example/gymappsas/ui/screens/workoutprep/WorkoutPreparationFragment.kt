package com.example.gymappsas.ui.screens.workoutprep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs


class WorkoutPreparationFragment : Fragment() {
    private val workoutViewModel: WorkoutPreparationViewModel by activityViewModels()
    private val args: WorkoutPreparationFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            workoutViewModel.getWorkoutById(args.workoutId)
            setContent {
                MaterialTheme {
                    WorkoutPreparationScreen(
                        viewModel = workoutViewModel,
                        onSetClick = { workoutViewModel.getSelectedExerciseWorkout(it) },
                        onCancelClick = { workoutViewModel.toggleOffSetExerciseWeightDialog() },
                        getSelectedWeight = { workoutViewModel.getSelectedWeight(it) },
                        onConfirmClick = { workoutViewModel.updateExerciseWorkoutWeight() },
                        onBackClick = { findNavController().popBackStack() })
                }
            }
        }
    }
}
