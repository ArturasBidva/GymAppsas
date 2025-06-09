package com.example.gymappsas.ui.screens.profile

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.gymappsas.ui.screens.profilesetup.Gender

data class ProfileUiState(
    val isLoading: Boolean = true,
    val username : String = "",
    val profilePictureUrl: String? = null,
    val gender: Gender = Gender.NONE,
    val age: Int? = null,
    val weeklyWorkoutCount: Int? = null,
    val joinDate: String? = null,
    val settingsOptions: List<SettingsOption> = emptyList()
)

data class SettingsOption(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

