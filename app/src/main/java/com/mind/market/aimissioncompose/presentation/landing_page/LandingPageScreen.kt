package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.presentation.landing_page.components.Goal

@Composable
fun LandingPageScreen(
    viewModel: LandingPageViewModel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        FloatingActionButton(
            onClick = { navController.navigate(Route.ADD) }
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add goal")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val goals = state.goals
            items(goals.size) { index ->
                val goal = goals[index]
                Goal(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                        .clickable {
//                            viewModel.onEvent(LandingPageUiEvent.NavigateToDetailGoal(goal))
                            navController.navigate(Route.ADD + "?goalId=${goal.id}")
                        }
                        .padding(8.dp),
                    goal = goal,
                    onDeleteClicked = {
                        viewModel.onEvent(
                            LandingPageUiEvent.OnDeleteGoalClicked(
                                goal
                            )
                        )
                    },
                    onStatusChangeClicked = {
                        viewModel.onEvent(
                            LandingPageUiEvent.OnStatusChangedClicked(
                                goal
                            )
                        )
                    },
                    navController = navController
                )

                if (index < state.goals.size) {
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}