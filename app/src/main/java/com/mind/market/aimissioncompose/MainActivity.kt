package com.mind.market.aimissioncompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationScreen
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationViewModel
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.presentation.detail.DetailScreen
import com.mind.market.aimissioncompose.presentation.detail.DetailViewModel
import com.mind.market.aimissioncompose.presentation.information.InformationScreen
import com.mind.market.aimissioncompose.presentation.information.InformationViewModel
import com.mind.market.aimissioncompose.presentation.landing_page.LandingPageScreen
import com.mind.market.aimissioncompose.presentation.landing_page.LandingPageViewModel
import com.mind.market.aimissioncompose.presentation.settings.SettingsScreen
import com.mind.market.aimissioncompose.presentation.settings.SettingsViewModel
import com.mind.market.aimissioncompose.statistics.presentation.StatisticsScreen
import com.mind.market.aimissioncompose.statistics.presentation.StatisticsViewModel
import com.mind.market.aimissioncompose.ui.theme.AimissionComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var auth: FirebaseAuth

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        initFirebaseAuth()

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
                                IconButton(
                                    onClick = {
                                        navController.navigate(Route.ADD)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Create,
                                        contentDescription = "Add new goal"
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        navController.navigate(Route.STATISTICS)
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Star,
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
                            startDestination = if (auth.currentUser != null) Route.LANDING_PAGE else Route.AUTHENTICATION
                        ) {
                            composable(Route.LANDING_PAGE) {
                                Surface(
                                    modifier = Modifier.fillMaxSize(),
                                    color = MaterialTheme.colors.background
                                ) {
                                    val viewModel = hiltViewModel<LandingPageViewModel>()
                                    val state by viewModel.state.collectAsState()
                                    LandingPageScreen(
                                        navController = navController,
                                        state = state,
                                        uiEvent = viewModel.uiEvent,
                                        commandProcessor = viewModel::processCommand,
                                        onShowFeedbackDialog = ::showFeedbackDialog
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
                                val viewModel = hiltViewModel<DetailViewModel>()
                                val state by viewModel.state.collectAsState()
                                DetailScreen(
                                    navController = navController,
                                    state = state,
                                    uiEvent = viewModel.uiEvent,
                                    onEvent = viewModel::onEvent
                                )
                            }
                            composable(
                                route = Route.INFORMATION
                            ) {
                                val viewModel = hiltViewModel<InformationViewModel>()
                                val state by viewModel.state.collectAsState()
                                InformationScreen(
                                    state = state,
                                    onEvent = viewModel::onEvent
                                )
                            }
                            composable(
                                route = Route.SETTINGS
                            ) {
                                val viewModel = hiltViewModel<SettingsViewModel>()
                                val state = viewModel.settingsState
                                SettingsScreen(
                                    state = state,
                                    onEvent = viewModel::onEvent,
                                    navController = navController
                                )
                            }
                            composable(
                                route = Route.STATISTICS
                            ) {
                                val viewModel = hiltViewModel<StatisticsViewModel>()
                                val state by viewModel.state.collectAsState()
                                StatisticsScreen(
                                    state = state
                                )
                            }
                            composable(
                                route = Route.AUTHENTICATION
                            ) {
                                val viewModel = hiltViewModel<AuthenticationViewModel>()
                                val state by viewModel.state.collectAsState()
                                AuthenticationScreen(
                                    state = state,
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    private fun initFirebaseAuth() {
        auth = Firebase.auth
    }

    private fun showFeedbackDialog() {
        val reviewManager = ReviewManagerFactory.create(applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                reviewManager.launchReviewFlow(this, it.result)
            }
        }
    }
}