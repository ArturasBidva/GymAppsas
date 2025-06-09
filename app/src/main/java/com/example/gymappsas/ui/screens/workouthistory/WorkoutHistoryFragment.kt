package com.example.gymappsas.ui.screens.workouthistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutHistoryFragment : Fragment() {
    private val workoutHistoryViewModel: WorkoutHistoryViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                WorkoutHistory(
                    workoutHistoryViewModel = workoutHistoryViewModel,
                    onNavigateClick = { /*TODO*/ },
                    onExerciseClick = {},
                    onBackClick = {findNavController().popBackStack()}
                )
            }
        }
    }
}