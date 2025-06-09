package com.example.gymappsas.ui.screens.createexercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gymappsas.ui.screens.exercisesbycategory.ExerciseViewModel

class CreateExerciseFragment : Fragment() {
    private val createExerciseViewModel: CreateExerciseViewModel by activityViewModels()
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

            }
        }
    }
}