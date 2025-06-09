package com.example.gymappsas.ui.screens.exercisesbyselectedcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs

class ExercisesBySelectedCategoryFragment : Fragment() {
    private val exerciseBySelectedCategoryViewModel: ExerciseBySelectedCategoryViewModel by activityViewModels()
    private val args: ExercisesBySelectedCategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        exerciseBySelectedCategoryViewModel.getAllExercisesBySelectedCategory(categoryName = args.exerciseCategory)
        return ComposeView(requireContext()).apply {
            setContent {
                ExercisesBySelectedCategory(
                    exerciseBySelectedCategoryViewModel = exerciseBySelectedCategoryViewModel,
                    onNavigateBack = { findNavController().popBackStack() },
                    onSelectExerciseClick = {
                        val action =
                            ExercisesBySelectedCategoryFragmentDirections.actionExercisesBySelectedCategoryFragmentToExerciseDetailsFragment(
                            exerciseId = it
                            )
                        findNavController().navigate(action)
                    })
            }
        }
    }
}