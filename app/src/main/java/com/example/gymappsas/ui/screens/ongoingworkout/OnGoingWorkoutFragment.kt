package com.example.gymappsas.ui.screens.ongoingworkout

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnGoingWorkoutFragment : Fragment() {
    private val onGoingWorkoutViewModel: OnGoingWorkoutViewModel by activityViewModels()
    private val args : OnGoingWorkoutFragmentArgs by navArgs()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notifications Permission Granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(requireContext(), "Notifications Permission Denied", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
onGoingWorkoutViewModel.getOnGoingWorkout(workoutId = args.workoutId)
        return ComposeView(requireContext()).apply {
            setContent {
                OnGoingWorkout(onGoingWorkoutViewModel = onGoingWorkoutViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}

