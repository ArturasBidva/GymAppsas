package com.example.gymappsas.ui.screens.exercisedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs

class ExerciseDetailsFragment : Fragment() {
    private val exerciseDetailsViewModel: ExerciseDetailsViewModel by activityViewModels()
    private val args: ExerciseDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       exerciseDetailsViewModel.getSelectedExercise(args.exerciseId)
        return ComposeView(requireContext()).apply {
            setContent {
                ExerciseDetails(exerciseDetailsViewModel = exerciseDetailsViewModel, popStackBack = {findNavController().popBackStack()})
            }
        }
    }

}