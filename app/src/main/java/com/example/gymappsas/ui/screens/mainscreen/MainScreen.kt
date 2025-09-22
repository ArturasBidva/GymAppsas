package com.example.gymappsas.ui.screens.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.gymappsas.BottomNavigationBar
import com.example.gymappsas.R
import com.example.gymappsas.data.db.models.profile.Profile
import com.example.gymappsas.data.repository.fitness.FitnessData
import com.example.gymappsas.ui.screens.profilesetup.FitnessLevel

@Composable
fun MainScreen(
    mainScreenViewModel: MainViewModel = hiltViewModel(),
    navigateToWorkoutScreen: () -> Unit
) {
    val uiState by mainScreenViewModel.uiState.collectAsState(MainScreenUiState())

    Content(
        profile = uiState.profile,
        fitnessData = uiState.todayFitnessData,
        isWorkoutToday = mainScreenViewModel.ifYouHaveWorkoutToday(),
        ifYouHaveWorkouts = mainScreenViewModel.ifYouHaveWorkouts(),
        navigateToWorkoutScreen = {navigateToWorkoutScreen()}
    )
}

@Composable
private fun Content(
    profile: Profile,
    fitnessData: FitnessData? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isWorkoutToday: Boolean,
    hasRecentActivity: Boolean = true,
    ifYouHaveWorkouts: Boolean = false,
    navigateToWorkoutScreen : () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7FAFC))
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Hello, ${profile.name}!",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Text(
                "Let's crush your fitness goals today",
                color = Color.Gray
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Stats Grid - Updated to use real fitness data
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.progress_trend_icon,
                        iconColor = Color(0xFF2563EB),
                        title = "Steps Today",
                        value = "${fitnessData?.steps ?: 0}",
                        subtext = "steps taken",
                        backgroundColor = Color(0xFFEFF6FF),
                    )
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.fire_calories_icon,
                        iconColor = Color(0xFFF97316),
                        title = "Calories",
                        value = "${fitnessData?.calories?.toInt() ?: 520}",
                        subtext = "burned today",
                        backgroundColor = Color(0xFFFFF7ED),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.calendar_streak_icon,
                        iconColor = Color(0xFF22C55E),
                        title = "Distance",
                        value = "${String.format("%.1f", fitnessData?.distance ?: 0f)} km",
                        subtext = "traveled today",
                        backgroundColor = Color(0xFFF0FDF4),
                    )
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        iconRes = R.drawable.level_award_icon,
                        iconColor = Color(0xFFA855F7),
                        title = "Level",
                        value = profile.fitnessLevel?.displayName ?: "Unknown",
                        subtext = "Active: ${fitnessData?.activeMinutes ?: 0} min",
                        backgroundColor = Color(0xFFFAF5FF),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            SectionHeader(title = "Today's Plan", actionText = "View All")
            if (isWorkoutToday) {
                WorkoutItemCard(
                    title = "Upper Body Strength",
                    duration = "45 min",
                    description = "Focus on chest, shoulders and triceps",
                    buttonColor = Color(0xFF3B82F6),
                    textColor = Color.White
                )
            } else if (ifYouHaveWorkouts) {
                NoWorkoutTodayCard(scheduledDays = profile.workoutDays)
            } else {
                PlanYourFitnessJourneyCard()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Activity
            SectionHeader(title = "Recent Activity", actionText = "See All")

            if (hasRecentActivity) {
                ActivityItem(
                    iconRes = R.drawable.workouts_icon_2,
                    iconBackground = Color(0xFFEFF6FF),
                    title = "Leg Day Workout",
                    time = "Yesterday, 6:30 PM",
                    duration = "35 min"
                )
                Spacer(modifier = Modifier.height(8.dp))
                ActivityItem(
                    iconRes = R.drawable.calendar_streak_icon,
                    iconBackground = Color(0xFFF0FDF4),
                    title = "Morning Run",
                    time = "Yesterday, 7:15 AM",
                    duration = "22 min"
                )
            } else {
                NoRecentActivityCard(onStartWorkoutClick = { navigateToWorkoutScreen() })
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PlanYourFitnessJourneyCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Illustration
            Image(
                painter = painterResource(id = R.drawable.fitness_journey),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Plan Your Fitness Journey",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Add workouts to your calendar to stay consistent and track your progress.",
                fontSize = 14.sp,
                color = Color(0xFF374151),
                lineHeight = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primary button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2563EB), RoundedCornerShape(8.dp))
                    .clickable { /* TODO: open calendar */ }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Add Workout to Calendar",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenWithBottomBarPreview() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onInformationClick = {}
            )
        }
    ) { contentPadding ->
        Content(
            profile = Profile(
                name = "Arturas",
                workoutDays = listOf("Monday", "Wednesday"),
                fitnessLevel = FitnessLevel.EXPERT,
                bmi = 0.5f
            ),
            fitnessData = FitnessData(
                steps = 5420,
                calories = 520f,
                distance = 3.2f,
                activeMinutes = 45,
                heartRate = 72,
                date = "2025-01-29"
            ),
            contentPadding = contentPadding,
            isWorkoutToday = false,
            hasRecentActivity = false,
            ifYouHaveWorkouts = false
        )
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    iconColor: Color,
    title: String,
    value: String,
    subtext: String,
    backgroundColor: Color
) {
    Column(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        Text(
            value,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            subtext,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun SectionHeader(title: String, actionText: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            actionText,
            color = Color(0xFF3B82F6),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ActivityItem(
    iconRes: Int,
    iconBackground: Color,
    title: String,
    time: String,
    duration: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(iconBackground, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                time,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Text(
            duration,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}

@Composable
fun WorkoutItemCard(
    title: String,
    duration: String,
    description: String,
    buttonColor: Color,
    textColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Workout Image
            Image(
                painter = painterResource(id = R.drawable.workouts_icon_2),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            // Content
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        duration,
                        fontSize = 12.sp,
                        color = Color(0xFF3B82F6),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .background(
                                Color(0xFFEFF6FF),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(buttonColor, RoundedCornerShape(8.dp))
                        .clickable { /* TODO: navigate to workout details */ }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Show Workout",
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun NoWorkoutTodayCard(
    modifier: Modifier = Modifier,
    scheduledDays: List<String> = emptyList()
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFFFF7E6), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFEF3C7)   // ← #FEF3C7
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.calendericon),
                contentDescription = null,
                tint = Color(0xFFC7A364),
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Top)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text("Rest Day", fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Spacer(Modifier.height(2.dp))
                Text(
                    "Today is not in your selected workout schedule. Consider light activity or recovery.",
                    fontSize = 14.sp,
                    color = Color(0xFF374151)
                )
                Spacer(Modifier.height(12.dp))
                if (scheduledDays.isNotEmpty()) {
                    Text(
                        "Your workout days: ${scheduledDays.joinToString()}",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
fun NoRecentActivityCard(onStartWorkoutClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.clock),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Recent Activity",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You haven't logged any workouts yet. Start",
            fontSize = 14.sp,
            color = Color(0xFF374151),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "your fitness journey today!",
            fontSize = 14.sp,
            color = Color(0xFF374151),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .background(Color(0xFF2563EB), RoundedCornerShape(8.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .clickable { onStartWorkoutClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Start Your First Workout",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
