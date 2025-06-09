package com.example.gymappsas.ui.screens.createworkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController


class CreateWorkoutFragment : Fragment() {
    private val createWorkoutViewModel: CreateWorkoutViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

                CreateWorkoutScreen(
                    createWorkoutViewModel = createWorkoutViewModel,
                    onBackClick = {
                        findNavController().popBackStack()
                    },
                    onSelectedCategory = { createWorkoutViewModel.getSelectedCategories(it) },
                    navigateToAddExerciseToWorkoutFragment = {
                    createWorkoutViewModel.validateFields()
                        if(createWorkoutViewModel.uiState.value.hasTitleError || createWorkoutViewModel.uiState.value.hasDescriptionError){

                        }else{
                            val action =
                                CreateWorkoutFragmentDirections.actionCreateWorkoutFragmentToAddExerciseToWorkoutFragment()
                            findNavController().navigate(action)
                        }
                    },
                    onWorkoutDescriptionChange = { createWorkoutViewModel.updateDescription(it) },
                    onWorkoutTitleChange = { createWorkoutViewModel.updateTitle(it) }
                )
            }
        }
    }
}