package com.mind.market.aimissioncompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationScreen
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationViewModel
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.presentation.detail.DetailScreen
import com.mind.market.aimissioncompose.presentation.information.InformationScreen
import com.mind.market.aimissioncompose.presentation.landing_page.LandingPageScreen
import com.mind.market.aimissioncompose.presentation.settings.SettingsScreen
import com.mind.market.aimissioncompose.statistics.presentation.StatisticsScreen
import com.mind.market.aimissioncompose.statistics.presentation.StatisticsViewModel
import com.mind.market.aimissioncompose.ui.theme.AimissionComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            AimissionComposeTheme {
                val navController = rememberNavController()
                val scaffoldState: ScaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "AimissionCompose")
                            },
                            actions = {
                                // RowScope here, so these icons will be placed horizontally
                                IconButton(
                                    onClick = {
                                        navController.navigate(Route.ADD)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Create,
                                        contentDescription = "Open Statistics"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        navController.navigate(Route.STATISTICS)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Create,
                                        contentDescription = "Open statistics"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        navController.navigate(Route.SETTINGS)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Settings,
                                        contentDescription = "Open settings"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        navController.navigate(Route.INFORMATION)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Info,
                                        contentDescription = "Open app info"
                                    )
                                }
                            },
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White,
                            elevation = 10.dp
                        )
                    }, content = {
                        NavHost(
                            navController = navController,
                            startDestination = Route.AUTHENTICATION//Route.LANDING_PAGE
                        ) {
                            composable(Route.LANDING_PAGE) {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colors.background
                                ) {
                                    LandingPageScreen(
                                        navController = navController
                                    )
                                }
                            }
                            composable(
                                route = Route.ADD + "?goalId={goalId}",
                                arguments = listOf(navArgument(
                                    name = "goalId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                                )
                            ) {
                                DetailScreen(
                                    navController = navController
                                )
                            }
                            composable(
                                route = Route.INFORMATION
                            ) {
                                InformationScreen(
                                    navController = navController
                                )
                            }
                            composable(
                                route = Route.SETTINGS
                            ) {
                                SettingsScreen(
                                    navController = navController
                                )
                            }
                            composable(
                                route = Route.STATISTICS
                            ) {
                                val viewModel = hiltViewModel<StatisticsViewModel>()
                                val state by viewModel.state.collectAsState()
                                StatisticsScreen(
                                    state = state,
                                    onEvent = viewModel::onEvent
                                )
                            }
                            composable(
                                route = Route.AUTHENTICATION
                            ) {
                                val viewModel = hiltViewModel<AuthenticationViewModel>()
                                val state by viewModel.state.collectAsState()
                                AuthenticationScreen(
                                    state = state,
                                    onEvent = viewModel::onEvent
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}