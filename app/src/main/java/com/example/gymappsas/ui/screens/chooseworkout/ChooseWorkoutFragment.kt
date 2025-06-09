package com.example.gymappsas.ui.screens.chooseworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController


class ChooseWorkoutFragment : Fragment() {
    private val chooseWorkoutViewModel: ChooseWorkoutViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChooseWorkout(
                    viewModel = chooseWorkoutViewModel,
                    onStartClick = { workoutId ->
                        handleNavigation(workoutId)
                    },
                    onBackClick = { findNavController().popBackStack() }
                )
            }
        }
    }

    private fun handleNavigation(workoutId: Long) {
        chooseWorkoutViewModel.selectWorkout(workoutId)
        val navController = findNavController()
        val action = if (chooseWorkoutViewModel.checkIfWorkoutReadyToStart()) {
            ChooseWorkoutFragmentDirections.actionChooseWorkoutFragmentToOnGoingWorkoutFragment(
                workoutId = workoutId
            )
        } else {
            ChooseWorkoutFragmentDirections.actionChooseWorkoutFragmentToWorkoutPreparationFragment(
                workoutId = workoutId
            )
        }

        navController.navigate(action)
    }
}
