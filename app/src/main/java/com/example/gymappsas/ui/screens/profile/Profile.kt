package com.example.gymappsas.ui.screens.profile

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.util.MockProfileData

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profileUiState by profileViewModel.uiState.collectAsState()
    val profile = profileUiState.profile
    if (profile != null) {
        Content(profile = profile)
    }
}

@Composable
private fun Content(profile: Profile) {
    ProfileScreenComp(profile = profile, onEditProfile = {}, onLogout = {})
}

@Preview
@Composable
private fun ContentPreview() {
    Content(profile = MockProfileData.mockProfile)
}

@Composable
fun ProfileScreenComp(
    profile: Profile,
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFC))
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onEditProfile) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.Black
                )
            }
        }

        // Profile Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.male),
                    contentDescription = "Profile",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(96.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                profile.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text("Premium Member", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Stats Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("28", "Workouts", Color(0xFFEFF6FF), Modifier.weight(1f))
            StatCard("9.2K", "Calories", Color(0xFFF0FDF4), Modifier.weight(1f))
            StatCard("4.8", "Hours", Color(0xFFF5F3FF), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // User Stats
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Your Stats",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Weight", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            "${profile.weight} kg",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Column {
                        Text("Height", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            "${profile.height} cm",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Column {
                        Text("BMI", fontSize = 14.sp, color = Color.Gray)
                        Text("${profile.bmi}", fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    TextButton(onClick = { /* Update stats */ }) {
                        Text("Update", color = Color(0xFF3B82F6))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Goals",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                GoalProgress("Weekly workouts", 4, profile.weeklyWorkoutCount, Color(0xFF3B82F6))
                Spacer(modifier = Modifier.height(8.dp))
                GoalProgress("Monthly calories", 12400, 15000, Color(0xFF10B981))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Options
        OptionItem("Workout History", R.drawable.outline_calendar_today_24, Color(0xFFEFF6FF))
        OptionItem("Progress Tracking", R.drawable.outline_calendar_today_24, Color(0xFFF0FDF4))
        OptionItem("Achievements", R.drawable.outline_calendar_today_24, Color(0xFFF5F3FF))
        OptionItem("Notifications", R.drawable.outline_calendar_today_24, Color(0xFFFFF7ED))
        OptionItem("Help & Support", R.drawable.outline_calendar_today_24, Color(0xFFF3F4F6))

        // Logout
        Spacer(modifier = Modifier.height(16.dp))
        OptionItem(
            "Log Out",
            R.drawable.outline_calendar_today_24,
            Color(0xFFFEF2F2),
            isLogout = true,
            onClick = onLogout
        )
    }
}

@Composable
fun StatCard(value: String, label: String, bgColor: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 4.dp)
            .background(bgColor, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp)
    ) {
        Text(
            value,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Text(label, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
fun GoalProgress(label: String, current: Int, goal: Int, progressColor: Color) {
    val progress = current.toFloat() / goal
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 14.sp, color = Color.Black)
            Text(
                "$current/$goal",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(8.dp)
                    .background(progressColor, RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun OptionItem(
    title: String,
    @DrawableRes iconId: Int,
    bgColor: Color,
    isLogout: Boolean = false,
    onClick: () -> Unit = {}
) {
    val textColor = if (isLogout) Color(0xFFDC2626) else Color.Black
    val chevronColor = if (isLogout) Color(0xFFF87171) else Color.Gray
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(12.dp)
            .padding(horizontal = 8.dp)
            .then(Modifier.height(56.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = title,
                tint = if (isLogout) Color(0xFFDC2626) else Color.Gray
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, modifier = Modifier.weight(1f), color = textColor)
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = chevronColor
        )
    }
}