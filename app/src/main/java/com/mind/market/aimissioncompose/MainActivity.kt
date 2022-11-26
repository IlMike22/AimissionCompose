package com.mind.market.aimissioncompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.presentation.detail.DetailScreen
import com.mind.market.aimissioncompose.presentation.landing_page.LandingPageScreen
import com.mind.market.aimissioncompose.ui.theme.AimissionComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        Icons.Filled.Create,
                                        contentDescription = "Localized description"
                                    )
                                }
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(
                                        Icons.Filled.Settings,
                                        contentDescription = "Localized description"
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
                            startDestination = Route.LandingPage
                        ) {
                            composable(Route.LandingPage) {
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
                                })
                            ) {
                                DetailScreen(
                                    navController = navController
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}