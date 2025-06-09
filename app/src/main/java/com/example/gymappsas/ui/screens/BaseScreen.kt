
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymappsas.NavigationViewModel
import com.example.gymappsas.ui.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    isLoading: Boolean,
    topBarTitle: String,
    isDarkTheme: Boolean = true,
    navigate: (BaseScreenNavigation) -> Unit = {},
    content: @Composable (SnackbarHostState) -> Unit,
) {
    val navigationViewModel: NavigationViewModel = viewModel()
    val selectedItem by navigationViewModel.selectedTab.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    AppTheme(darkTheme = false) {
        val snackBarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = NavigationBarDefaults.containerColor,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    title = {
                        Text(
                            text = topBarTitle,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (topBarTitle != "Home") {
                            IconButton(onClick = { navigate(BaseScreenNavigation.Back) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = {
                val items = listOf("Home", "Profile", "Settings")
                val selectedIcons = listOf(
                    Icons.Filled.Home,
                    Icons.Filled.AccountCircle,
                    Icons.Filled.Settings
                )
                val unselectedIcons = listOf(
                    Icons.Outlined.Home,
                    Icons.Outlined.AccountCircle,
                    Icons.Outlined.Settings
                )

                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selectedItem == index)
                                        selectedIcons[index]
                                    else
                                        unselectedIcons[index],
                                    contentDescription = item
                                )
                            },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = {
                                    navigate(BaseScreenNavigation.fromRoute(item))
                            }
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackBarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {
                        content(snackBarHostState)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BaseScreenPreview() {
    BaseScreen(
        isLoading = false,
        topBarTitle = "Title",
        content = { snackbarHostState ->
            Text("Content")
        }
    )
}

sealed class BaseScreenNavigation(val route: String) {
    data object Home : BaseScreenNavigation("Home")
    data object Profile : BaseScreenNavigation("Profile")
    data object Settings : BaseScreenNavigation("Settings")
    data object Back : BaseScreenNavigation("Back")
    data object Other : BaseScreenNavigation("Other")

    companion object {
        fun fromRoute(route: String): BaseScreenNavigation = when (route) {
            "Home" -> Home
            "Profile" -> Profile
            "Settings" -> Settings
            "Back" -> Back
            "Other" -> Other
            else -> throw IllegalArgumentException("Unknown route: $route")
        }
    }
}


