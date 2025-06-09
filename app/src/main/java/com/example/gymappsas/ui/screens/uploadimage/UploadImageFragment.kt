package com.example.gymappsas.ui.screens.uploadimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class UploadImageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                UploadImageScreen(
                    popStackBack = { findNavController().popBackStack() },
                    navigateToCameraFragment = {val action = UploadImageFragmentDirections.actionUploadImageToCameraFragment()
                    findNavController().navigate(action)}
                )
            }
        }
    }
}