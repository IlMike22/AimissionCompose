package com.mind.market.aimissioncompose.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mind.market.aimissioncompose.auth.presentation.components.AuthenticationCreateUser
import com.mind.market.aimissioncompose.auth.presentation.components.AuthenticationLoginUser
import com.mind.market.aimissioncompose.navigation.Route
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel,
    navController: NavController,
    state: AuthenticationState,

) {
    LaunchedEffect("navigation") {
        viewModel.uiEvent.collectLatest { uiEvent ->
            when (uiEvent) {
                AuthenticationUiEvent.NavigateToLandingPageAfterLogin -> {
                    navController.navigate(Route.LANDING_PAGE)
                }
            }
        }
    }

    val pagerState = rememberPagerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            Tab(
                selected = pagerState.currentPage == 0,
                text = { Text(text = "Login") },
                onClick = { /*TODO*/ })
            Tab(
                selected = pagerState.currentPage == 1,
                text = { Text(text = "Register") },
                onClick = { /*TODO*/ })
        }
        HorizontalPager(count = 2, state = pagerState) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (page == 0) {
                    AuthenticationLoginUser(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        state = state,
                        onEvent = viewModel::onEvent,
                        navController = navController
                    )
                } else if (page == 1) {
                    AuthenticationCreateUser(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        state = state,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}