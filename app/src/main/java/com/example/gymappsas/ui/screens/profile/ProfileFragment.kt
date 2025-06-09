package com.example.gymappsas.ui.screens.profile

import BaseScreenNavigation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gymappsas.NavigationViewModel

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val navigationViewModel: NavigationViewModel by viewModels()
        navigationViewModel.syncTabWithRoute("Profile")
        return ComposeView(requireContext()).apply {
            setContent {
                ProfileScreen(profileViewModel = profileViewModel, navigate = {
                    when (it) {
                        BaseScreenNavigation.Profile -> {}
                        BaseScreenNavigation.Home -> {
                            navigateHome()
                        }

                        BaseScreenNavigation.Settings -> {}
                        BaseScreenNavigation.Back -> {
                        }

                        else -> {}
                    }
                })
            }
        }
    }

    private fun navigateHome() {
        val action = ProfileFragmentDirections.actionProfileSetupFragmentToMainScreenFragment()
        findNavController().navigate(action)
    }
}
