package com.example.gymappsas.ui.screens.profile

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gymappsas.data.db.models.profile.Profile

data class ProfileUiState(
    val profile : Profile? = null,
    val isLoading: Boolean = true,
    val profilePictureUrl: String? = null,
    val weeklyWorkoutCount: Int? = null,
    val settingsOptions: List<SettingsOption> = emptyList()
)

data class SettingsOption(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

