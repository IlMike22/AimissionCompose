package com.mind.market.aimissioncompose.presentation.landing_page

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import com.mind.market.aimissioncompose.presentation.landing_page.components.Goal
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LandingPageScreen(
    viewModel: LandingPageViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val detailPageScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("invalidate")?.observeAsState()
    detailPageScreenResult?.value?.let { isUpdateList ->
        if (isUpdateList) {
            viewModel.getGoals()
            viewModel.onEvent(LandingPageUiEvent.OnListUpdated)
            navController.currentBackStackEntry?.savedStateHandle?.set("invalidate", false)
        }
    }

    val sheetState: BottomSheetState =
        rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )


    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is LandingPageUiEvent.ShowSnackbar -> {
                    val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = if (event.snackbarAction == SnackBarAction.UNDO) "Undo" else null
                    )
                    when (snackBarResult) {
                        SnackbarResult.ActionPerformed -> {
                            when (event.snackbarAction) {
                                SnackBarAction.UNDO -> {
                                    viewModel.onEvent(LandingPageUiEvent.OnUndoDeleteGoalClicked)
                                }
                            }
                        }
                        SnackbarResult.Dismissed -> Unit
                    }
                }
            }
        }
//        if (state.showSnackbar) {
//            val result = scaffoldState.snackbarHostState.showSnackbar(
//                message = state.snackbarMessage,
//                actionLabel = "Undo"
//            )
//
//            when (result) {
//                SnackbarResult.ActionPerformed -> {
//                    viewModel.onEvent(LandingPageUiEvent.OnUndoDeleteGoalClicked)
//                }
//                SnackbarResult.Dismissed -> Unit
//            }
//        }
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState
    ) {
        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bottom Sheet Content",
                        fontSize = 16.sp,
                    )
                }
            },
            sheetBackgroundColor = Color.Blue,
            sheetPeekHeight = 36.dp,

            ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            if (sheetState.isCollapsed) sheetState.expand() else sheetState.collapse()
                        }
                    }) {
                    Text("Show/Hide Bottom Sheet")
                }
            }
        }

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
                                navController.navigate(Route.ADD + "?goalId=${goal.id}")
                            }
                            .padding(8.dp),
                        goal = goal,
                        onDeleteClicked = { goalToDelete ->
                            viewModel.onEvent(
                                LandingPageUiEvent.OnDeleteGoalClicked(goalToDelete)
                            )
                        },
                        onStatusChangeClicked = { selectedGoal ->
                            viewModel.onEvent(
                                LandingPageUiEvent.OnStatusChangedClicked(selectedGoal)
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
}