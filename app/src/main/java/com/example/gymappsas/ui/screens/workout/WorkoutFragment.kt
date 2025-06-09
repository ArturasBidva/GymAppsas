package com.example.gymappsas.ui.screens.workout

import BaseScreenNavigation
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

class WorkoutFragment : Fragment() {

    private val workoutViewModel: WorkoutViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WorkoutScreen(
                    workoutViewModel = workoutViewModel,
                    onBackClick = { findNavController().popBackStack() },
                    onSelectWorkoutClick = {
                        val action =
                            WorkoutFragmentDirections.actionWorkoutFragmentToWorkoutDetailsFragment(
                                it
                            )
                        findNavController().navigate(action)
                    },
                    onCreateWorkoutClick = {
                        val action =
                            WorkoutFragmentDirections.actionWorkoutFragmentToWorkoutCreateWorkoutFragment()
                        findNavController().navigate(action)
                    },
                    onClickSeeMore = {

                    },
                    onDeleteClick = { workoutViewModel.deleteWorkoutById(it) },
                    onSearchBarTextValueChange = { workoutViewModel.updateSearchText(it) },
                    navigate = {
                        Log.d("WorkoutFragment", "Navigation triggered: $it")
                        when (it) {
                            BaseScreenNavigation.Home -> {
                                navigateHome()
                            }

                            BaseScreenNavigation.Settings -> {

                            }

                            BaseScreenNavigation.Back -> {

                            }
                            
                            BaseScreenNavigation.Profile -> {
                                val action =
                                    WorkoutFragmentDirections.actionWorkoutFragmentToToProfileFragment()
                                findNavController().navigate(action)
                            }

                            else -> {}
                        }
                    }
                )
            }
        }
    }
    private fun navigateHome() {
        val action = WorkoutFragmentDirections.actionWorkoutFragmentToMainScreenFragment()
        findNavController().navigate(action)
    }
}
