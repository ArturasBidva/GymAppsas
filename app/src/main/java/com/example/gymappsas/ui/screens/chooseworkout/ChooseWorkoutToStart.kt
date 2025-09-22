package com.example.gymappsas.ui.screens.chooseworkout

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.gymappsas.BottomNavigationBar
import com.example.gymappsas.data.db.models.workouts.Workout
import com.example.gymappsas.ui.screens.workout.CompactWorkoutCard
import com.example.gymappsas.util.MockWorkoutData

@Composable
fun ChooseWorkout(
    viewModel: ChooseWorkoutViewModel,
    onStartClick: (Long) -> Unit
) {
    val workoutUiState by viewModel.uiState.collectAsState()
    val workouts = workoutUiState.workouts
    Content(workouts = workouts, onStartClick = { onStartClick(it) })
}

@Composable
private fun Content(
    workouts: List<Workout>,
    onStartClick: (Long) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val filters = listOf("All", "Strength", "Cardio", "Yoga", "Core")

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onInformationClick = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .verticalScroll(scrollState)
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search workouts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(24.dp)),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                )
            )

            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filters.forEach { filter ->
                    FilterChip(
                        label = { Text(filter) },
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF3D7BF4),
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFEDEDED),
                            labelColor = Color.Black
                        )
                    )
                }

                // Optional: Add filter icon chip
                FilterChip(
                    onClick = { /* open filter dialog */ },
                    selected = false,
                    label = {},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFFEDEDED)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Recommended Workouts",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filter and display workouts
            val filteredWorkouts = workouts.filter {
                (selectedFilter == "All" || it.category?.displayName.equals(selectedFilter)) &&
                        (searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true))
            }

            filteredWorkouts.forEach { workout ->
                CompactWorkoutCard(
                    workout = workout,
                    onStartClick = onStartClick
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChooseWorkoutPrev() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onInformationClick = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Content(workouts = MockWorkoutData.mockWorkouts) {}
        }
    }
}

@Preview
@Composable
private fun BottomNavPreview() {
    val navController = rememberNavController()
    Column {
        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(
            navController = navController,
            onInformationClick = {}
        )
    }
}
