package com.mind.market.aimissioncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AimissionComposeTheme {
                val navController = rememberNavController()
                val scaffoldState: ScaffoldState = rememberScaffoldState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
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
                                onNavigateToLandingPage = { navController.navigate(Route.LandingPage) }
                            )
                        }
                    }
                }
                // A surface container using the 'background' color from the theme

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AimissionComposeTheme {
        Greeting("Android")
    }
}