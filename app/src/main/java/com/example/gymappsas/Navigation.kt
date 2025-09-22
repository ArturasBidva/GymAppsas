package com.example.gymappsas

import PrepareWorkoutScreen
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.gymappsas.ui.screens.createworkout.CreateWorkoutScreen
import com.example.gymappsas.ui.screens.createworkout.CreateWorkoutViewModel
import com.example.gymappsas.ui.screens.exercisedetails.ExerciseDetails
import com.example.gymappsas.ui.screens.exercisedetails.ExerciseDetailsViewModel
import com.example.gymappsas.ui.screens.exercisesbycategory.ExerciseViewModel
import com.example.gymappsas.ui.screens.exercisesbycategory.ExercisesScreen
import com.example.gymappsas.ui.screens.exercisesbyselectedcategory.ExerciseBySelectedCategoryViewModel
import com.example.gymappsas.ui.screens.exercisesbyselectedcategory.ExercisesBySelectedCategory
import com.example.gymappsas.ui.screens.mainscreen.MainScreen
import com.example.gymappsas.ui.screens.ongoingworkout.OnGoingWorkoutScreen
import com.example.gymappsas.ui.screens.ongoingworkout.OnGoingWorkoutViewModel
import com.example.gymappsas.ui.screens.profile.ProfileScreen
import com.example.gymappsas.ui.screens.profilesetup.ProfileSetup
import com.example.gymappsas.ui.screens.profilesetup.ProfileSetupViewModel
import com.example.gymappsas.ui.screens.workout.WorkoutScreen
import com.example.gymappsas.ui.screens.workout.WorkoutViewModel
import com.example.gymappsas.ui.screens.workouthistory.WorkoutHistoryViewModel
import com.example.gymappsas.ui.screens.workouthistory.WorkoutVariantsScreen
import com.example.gymappsas.ui.screens.workoutprep.WorkoutPreparationViewModel
import com.example.gymappsas.ui.screens.workoutschedule.WorkoutScheduleScreen
import com.example.gymappsas.ui.screens.workoutschedule.WorkoutScheduleViewModel

@Composable
fun Navigation() {

    val navController = rememberNavController()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onInformationClick = {}
            )
        }
    ) { paddings ->
        NavHost(
            navController = navController,
            startDestination = MainGraph,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddings)
        ) {
            navigation<MainGraph>(startDestination = ProfileSetupScreen) {
                composable<MainScreen> {
                    MainScreen(navigateToWorkoutScreen = { navController.navigate(WorkoutScreen) })
                }
                composable<ProfileScreen> {
                    ProfileScreen()
                }
                composable<ProfileSetupScreen> {
                    val viewModel = hiltViewModel<ProfileSetupViewModel>()
                    val state by viewModel.state.collectAsState()

                    LaunchedEffect(state) {
                        if (state is ProfileSetupViewModel.ProfileSetupState.Loaded &&
                            (state as ProfileSetupViewModel.ProfileSetupState.Loaded).userExists
                        ) {
                            navController.navigate(MainScreen) {
                                popUpTo(ProfileSetupScreen) { inclusive = true }
                            }
                        }
                    }

                    if (state !is ProfileSetupViewModel.ProfileSetupState.Loaded ||
                        !(state as ProfileSetupViewModel.ProfileSetupState.Loaded).userExists
                    ) {
                        ProfileSetup(
                            profileSetupViewModel = viewModel,

                            )
                    }
                }

                composable<WorkoutScreen> {
                    val viewModel = hiltViewModel<WorkoutViewModel>()
                    WorkoutScreen(
                        workoutViewModel = viewModel,
                        onCreateWorkoutClick = { navController.navigate(CreateWorkoutScreen) },
                        onSearchChange = { viewModel.updateSearchText(it) },
                        onStartClick = { workoutId ->
                            viewModel.markVariantAsUsed(workoutId)
                            navController.navigate(WorkoutPreparationScreen(workoutId = workoutId))
                        },
                        onStarClick = { viewModel.addWorkoutToFavourites(it) },
                        onViewClick = { workoutId ->
                            navController.navigate(WorkoutVariantsScreen(workoutId = workoutId))
                        },
                        onMarkedStarClick = { viewModel.removeWorkoutFromFavourites(it) }
                    )
                }
                composable<WorkoutScheduleScreen> {
                    val viewModel = hiltViewModel<WorkoutScheduleViewModel>()
                    WorkoutScheduleScreen(
                        viewModel = viewModel,
                        popStackBack = { navController.popBackStack() }
                    )
                }
                composable<CreateWorkoutScreen> {
                    val viewModel = hiltViewModel<CreateWorkoutViewModel>()
                    CreateWorkoutScreen(
                        createWorkoutViewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onSelectedCategory = { viewModel.getSelectedCategories(it) },
                        onWorkoutTitleChange = { viewModel.updateTitle(it) },
                        onWorkoutDescriptionChange = { viewModel.updateDescription(it) },
                        navigateToExerciseDetails = {
                            navController.navigate(
                                ExerciseDetailsScreen(
                                    it
                                )
                            )
                        },
                        navigateToAddExerciseToWorkoutStep = { viewModel.navigateToAddExerciseToWorkout() },
                        onWorkoutCategoryChange = {
                            viewModel.updateCategory(it)
                        }
                    )
                }
                composable<ExerciseScreen> {
                    val exerciseViewModel = hiltViewModel<ExerciseViewModel>()
                    val workoutViewModel = hiltViewModel<WorkoutViewModel>()
                    ExercisesScreen(
                        exerciseViewModel = exerciseViewModel,
                        workoutViewModel = workoutViewModel,
                        navigateToExercisesByCategoryScreen = {
                            navController.navigate(
                                ExerciseBySelectedCategoryScreen(
                                    exerciseCategory = it
                                )
                            )
                        },
                        onExpandedChange = { expanded -> exerciseViewModel.isExpanded(expanded) })
                }
                composable<ExerciseBySelectedCategoryScreen> {
                    val viewModel = hiltViewModel<ExerciseBySelectedCategoryViewModel>()
                    ExercisesBySelectedCategory(
                        exerciseBySelectedCategoryViewModel = viewModel,
                        onSelectExerciseClick = {
                            navController.navigate(
                                ExerciseDetailsScreen(exerciseId = it)
                            )
                        },
                        onNavigateBack = { navController.popBackStack() })
                }
                composable<ExerciseDetailsScreen> {
                    val viewModel = hiltViewModel<ExerciseDetailsViewModel>()
                    ExerciseDetails(
                        exerciseDetailsViewModel = viewModel,
                        popStackBack = { navController.popBackStack() })
                }
                composable<OnGoingWorkoutScreen> { backStackEntry ->
                    val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
                    val viewModel = hiltViewModel<OnGoingWorkoutViewModel>()
                    LaunchedEffect(workoutId) {
                        viewModel.getOnGoingWorkout(workoutId = workoutId)
                    }

                    OnGoingWorkoutScreen(
                        onGoingWorkoutViewModel = viewModel,
                        onExpand = { viewModel.toggleExpanded(it) })
                }
                composable<WorkoutPreparationScreen> { backStackEntry ->
                    val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
                    val viewModel = hiltViewModel<WorkoutPreparationViewModel>()
                    LaunchedEffect(workoutId) {
                        viewModel.getWorkoutById(workoutId)
                    }
                    PrepareWorkoutScreen(
                        onStart = { navController.navigate(OnGoingWorkoutScreen(workoutId = it.id)) },
                        onBack = { viewModel.updateStepBack() },
                        viewModel = viewModel,
                        onSelectTrainingMethod = { viewModel.selectTrainingMethod(it) },
                        onUpdate = { viewModel.updateExerciseWorkoutData(it) },
                        handleNextStep = { viewModel.updateStep() },
                        isNextDisabled = { viewModel.isNextStepDisabled() },
                        onRestTimeChanged = { viewModel.updateRestTime(it) },
                        onEditWeights = { viewModel.onEditWeights() },
                        onCustomRestTimeChange = { viewModel.updateCustomRestTime(it) },
                        onSaveWorkout = { viewModel.saveWorkout() },
                        onNameChange = { viewModel.updateWorkoutVariantName(it) }
                    )
                }
                composable<WorkoutVariantsScreen> { backStackEntry ->
                    val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
                    val viewModel = hiltViewModel<WorkoutHistoryViewModel>()
                    WorkoutVariantsScreen(
                        workoutId = workoutId,
                        onBackClick = { navController.popBackStack() },
                        onStartNewWorkout = {
                            navController.navigate(
                                WorkoutPreparationScreen(
                                    workoutId
                                )
                            )
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onInformationClick: () -> Unit
) {
    val topLevelRoutes = listOf(
        TopLevelRoute(
            name = "Home",
            route = MainScreen,
            label = "Home",
            showSoon = false
        ),
        TopLevelRoute(
            name = "Workouts",
            route = WorkoutScreen,
            label = "Workouts",
            showSoon = false
        ),
        TopLevelRoute(
            name = "Exercises",
            route = ExerciseScreen,
            label = "Exercises",
            showSoon = false
        ),
        TopLevelRoute(
            name = "Schedule",
            route = WorkoutScheduleScreen,
            label = "Schedule",
            showSoon = false
        ),
        TopLevelRoute(
            name = "Profile",
            route = ProfileScreen,
            label = "Profile",
            showSoon = false
        ),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isInMainGraph = currentDestination?.hierarchy?.any {
        it.route?.contains(
            MainGraph::class.simpleName ?: ""
        ) == true
    } == true
    val hideBottomBar = currentDestination?.hierarchy?.any {
        it.hasRoute(CreateWorkoutScreen::class) ||
                it.hasRoute(ExerciseDetailsScreen::class) ||
                it.hasRoute(WorkoutPreparationScreen::class) ||
                it.hasRoute(WorkoutVariantsScreen::class) ||
                it.hasRoute(ProfileSetupScreen::class)
    } == true

    if (isInMainGraph && !hideBottomBar) {
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color(0xFF9CA3AF),
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(85.dp)
        ) {
            topLevelRoutes.forEach { topLevelRoute ->
                val routeSelected = currentDestination?.hierarchy?.any {
                    it.hasRoute(topLevelRoute.route::class)
                }
                if (routeSelected != null) {
                    NavigationBarItem(
                        selected = routeSelected,
                        onClick = {
                            if (topLevelRoute.name == "Information") {
                                onInformationClick()
                            } else if (!routeSelected) {
                                navController.navigate(topLevelRoute.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            // Use Material Icons instead of custom drawables
                            val icon = when (topLevelRoute.name) {
                                "Home" -> Icons.Default.Home
                                "Workouts" -> Icons.Default.Star
                                "Exercises" -> Icons.Default.List
                                "Schedule" -> Icons.Default.DateRange
                                "Profile" -> Icons.Default.Person
                                else -> Icons.Default.Home
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = topLevelRoute.label,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = topLevelRoute.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF2563EB),
                            unselectedIconColor = Color(0xFF9CA3AF),
                            selectedTextColor = Color(0xFF2563EB),
                            unselectedTextColor = Color(0xFF9CA3AF),
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val label: String,
    val showSoon: Boolean
)


@Preview(showBackground = true)
@Composable
private fun ComposableBarPreview() {
    BottomNavigationBar(
        navController = rememberNavController(),
        onInformationClick = {}
    )
}

@Composable
fun RequestNotificationPermissionIfNeeded() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Toast.makeText(
            context,
            if (isGranted) "Notifications Permission Granted" else "Notifications Permission Denied",
            Toast.LENGTH_SHORT
        ).show()
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(permission)
            }
        }
    }
}