package com.example.gymappsas.ui.screens.startworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartWorkoutFragment : Fragment() {
    private val workoutViewModel: StartWorkoutViewModel by activityViewModels()
    //private val args: StartWorkoutFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //workoutViewModel.getSelectedWorkout(args.myArg)
        return ComposeView(requireContext()).apply {
            setContent {
                StartWorkout(
                    viewModel = workoutViewModel,
                    onWorkoutComplete = { })
            }
        }
    }
}