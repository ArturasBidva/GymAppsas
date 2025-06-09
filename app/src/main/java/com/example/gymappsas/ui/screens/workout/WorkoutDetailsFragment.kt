package com.example.gymappsas.ui.screens.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs

class WorkoutDetailsFragment : Fragment() {
    private val workoutViewModel: WorkoutViewModel by activityViewModels()
    private val args: WorkoutDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                workoutViewModel.selectedWorkout(args.workoutId)
                WorkoutDetailsScreen(
                    viewModel = workoutViewModel,
                    navigateToExerciseDetails = {
                        val action =
                            WorkoutDetailsFragmentDirections.actionWorkoutDetailsFragmentToExerciseDetailsFragment(
                                it
                            )
                        findNavController().navigate(action)
                    },
                    onNavigateBackClick = { findNavController().popBackStack() },
                    onDeleteWorkoutClick = {
                        workoutViewModel.deleteWorkoutById(workoutId = it)
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }


}