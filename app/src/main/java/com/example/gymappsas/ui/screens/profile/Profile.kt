package com.example.gymappsas.ui.screens.profile

import BaseScreen
import BaseScreenNavigation
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymappsas.R

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = viewModel(),
    navigate: (BaseScreenNavigation) -> Unit
) {
    val profileUiState by profileViewModel.uiState.collectAsState()
    Content(profileUiState = profileUiState, navigate = { navigate(it) })
}

@Composable
private fun Content(profileUiState: ProfileUiState, navigate: (BaseScreenNavigation) -> Unit) {

    BaseScreen(
        topBarTitle = stringResource(id = R.string.profile),
        isLoading = false,
        isDarkTheme = false,
        navigate = { navigate(it) }
    )
    {
        ProfileScreenComp(profileUiState = profileUiState, onEditProfile = {}, onLogout = {})

    }
}


@Preview
@Composable
private fun ProfilePreview() {
    Content(profileUiState = ProfileUiState(), navigate = {})
}

@Composable
fun ProfileScreenComp(
    profileUiState: ProfileUiState,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture + Edit Icon
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(colorScheme.surfaceVariant)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.male),
                contentDescription = "Profile Picture",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            IconButton(
                onClick = onEditProfile,
                modifier = Modifier
                    .offset(6.dp, 6.dp)
                    .background(colorScheme.primary, CircleShape)
                    .size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = profileUiState.username,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )
        Text(
            text = "Member since ${profileUiState.joinDate}",
            fontSize = 14.sp,
            color = colorScheme.onBackground.copy(0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileStat(R.drawable.ic_workouts, "Workouts", "0")
            ProfileStat(R.drawable.ic_minutes, "Minutes/week", "0")
            ProfileStat(R.drawable.ic_calories, "Day Streak", "0")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Cards
        SettingsCard(icon = Icons.Default.Person, title = "Training Goals")
        SettingsCard(icon = Icons.Default.Settings, title = "App Settings")

        Spacer(modifier = Modifier.height(24.dp))

        // Logout
        TextButton(onClick = onLogout) {
            Text(
                text = "Log Out",
                color = colorScheme.error,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
@Composable
fun ProfileStat(@DrawableRes iconId: Int, label: String, value: String) {
    val colorScheme = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            tint = colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Text(text = value, fontWeight = FontWeight.Bold, color = colorScheme.onBackground)
        Text(text = label, fontSize = 12.sp, color = colorScheme.onBackground.copy(0.7f))
    }
}

@Composable
fun SettingsCard(icon: ImageVector, title: String) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, color = colorScheme.onSurface, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", tint = colorScheme.onSurface)
        }
    }
}