package com.example.gymappsas.ui.screens.exercisesbycategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.gymappsas.ui.screens.workout.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseFragment : Fragment() {
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()
    private val workoutViewModel: WorkoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ExercisesScreen(
                    exerciseViewModel = exerciseViewModel,
                    workoutViewModel = workoutViewModel,
                    navigateToCreateExercise = {
                        val action =
                            ExerciseFragmentDirections.actionExerciseFragmentToCreateExerciseFragment()
                        findNavController().navigate(action)
                    },
                    popStackBack = { findNavController().popBackStack() },
                    navigateToExercisesByCategoryScreen = {
                        val action =
                            ExerciseFragmentDirections.actionExerciseFragmentToExerciseBySelectedCategoryFragment(
                                it
                            )
                        findNavController().navigate(action)
                    }
                )
            }
        }
    }
}