package com.example.gymappsas.ui.screens.profilesetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gymappsas.ui.reusable.LoadingCircle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSetupFragment : Fragment() {
    private val profileSetupViewModel: ProfileSetupViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                val userState by profileSetupViewModel.state.collectAsState()

                when (userState) {
                    is ProfileSetupViewModel.ProfileSetupState.Loading -> {
                        LoadingCircle()
                    }

                    is ProfileSetupViewModel.ProfileSetupState.Loaded -> {
                        if ((userState as ProfileSetupViewModel.ProfileSetupState.Loaded).userExists) {
                            navigateToHomeScreen()
                        } else {
                            ProfileSetup(profileSetupViewModel = profileSetupViewModel, navigate = {
                                navigateToHomeScreen()
                            })
                        }
                    }
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        val action = ProfileSetupFragmentDirections.actionProfileSetupFragmentToMainScreenFragment()
        findNavController().navigate(action)
    }
}


