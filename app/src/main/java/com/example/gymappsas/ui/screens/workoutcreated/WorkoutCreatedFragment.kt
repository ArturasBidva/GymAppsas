package com.example.gymappsas.ui.screens.workoutcreated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import javax.inject.Inject

class WorkoutCreatedFragment @Inject constructor() :Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
           setContent {
              WorkoutCreated()
           }
        }
    }
}