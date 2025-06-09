package com.example.gymappsas.ui.screens.mainscreen

import BaseScreen
import BaseScreenNavigation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gymappsas.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navigationViewModel: NavigationViewModel by viewModels()
        navigationViewModel.syncTabWithRoute("Home")
        return ComposeView(requireContext()).apply {
            setContent {
                BaseScreen(
                    isLoading = false, topBarTitle = "Home", isDarkTheme = true,
                    navigate = {
                        when (it) {
                            BaseScreenNavigation.Profile -> navigateToProfilePage()
                            BaseScreenNavigation.Home -> {}
                            BaseScreenNavigation.Settings -> {}
                            BaseScreenNavigation.Back -> {
                                findNavController().popBackStack()
                            }

                            else -> {}
                        }
                    },
                    content = {
                        MainScreen(
                            navigateToWorkout = ::navigateToWorkout,
                            navigateToExercise = ::navigateToExercise,
                            navigateToWorkoutSchedule = ::navigateToWorkoutSchedule,
                            navigateToChooseWorkout = ::navigateToChooseWorkout,
                            navigateToWorkoutHistory = ::navigateToWorkoutHistory,
                        )
                    }
                )
            }
        }
    }


    private fun navigateToWorkout() {
        val action =
            MainScreenFragmentDirections.actionMainScreenFragmentToWorkoutsFragment()
        findNavController().navigate(action)
    }

    private fun navigateToExercise() {
        val action =
            MainScreenFragmentDirections.actionMainScreenFragmentToExerciseScreen()
        findNavController().navigate(action)
    }

    private fun navigateToWorkoutHistory() {
        val action =
            MainScreenFragmentDirections.actionMainScreenFragmentToWorkoutHistoryFragment()
        findNavController().navigate(action)
    }

    private fun navigateToChooseWorkout() {
        val action =
            MainScreenFragmentDirections.actionMainScreenFragmentToChooseWorkoutFragment()
        findNavController().navigate(action)
    }

    private fun navigateToWorkoutSchedule() {
        val action =
            MainScreenFragmentDirections.actionMainScreenFragmentToWorkoutSchedule()
        findNavController().navigate(action)

    }

    private fun navigateToProfilePage() {
        val action =
            MainScreenFragmentDirections.actionMainScreenFragmentToProfileFragment()
        findNavController().navigate(action)
    }

}
