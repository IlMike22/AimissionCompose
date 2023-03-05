package com.mind.market.aimissioncompose.presentation.landing_page

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import com.mind.market.aimissioncompose.presentation.landing_page.components.Goal
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LandingPageScreen(
    state: LandingPageState,
    modifier: Modifier = Modifier,
    uiEvent: Flow<LandingPageUiEvent>,
    onEvent: (LandingPageUiEvent) -> Unit,
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    val alertDialogState = rememberMaterialDialogState()

    val detailPageScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("invalidate")?.observeAsState()
    detailPageScreenResult?.value?.let { isUpdateList ->
        if (isUpdateList) {
            onEvent(LandingPageUiEvent.OnGoalUpdate)
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
        uiEvent.collect { event ->
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
                                    onEvent(LandingPageUiEvent.OnUndoDeleteGoalClicked)
                                }
                            }
                        }
                        SnackbarResult.Dismissed -> Unit
                    }
                }
                is LandingPageUiEvent.ShowGoalOverdueDialog -> {
                    alertDialogState.show()
                }
            }
        }
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
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp),
                        onClick = {
                            onEvent(LandingPageUiEvent.OnShowStatisticClicked)
                        }
                    ) { Text(text = "Show statistic") }

                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp),
                        onClick = {
                            onEvent(LandingPageUiEvent.OnLogoutUserClicked)
                        }
                    ) { Text(text = "Logout user") }
                }
            },
            sheetBackgroundColor = Color.Blue,
            sheetPeekHeight = 36.dp,

            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                if (state.isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    FloatingActionButton(
                        onClick = { navController.navigate(Route.ADD) }
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add goal")
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val goals = state.goals

                        items(goals) { goal ->
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
                                    onEvent(LandingPageUiEvent.OnDeleteGoalClicked(goalToDelete))
                                },
                                onStatusChangeClicked = { selectedGoal ->
                                    onEvent(LandingPageUiEvent.OnStatusChangedClicked(selectedGoal))
                                },
                                navController = navController
                            )

                            Divider(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    }
                }

                MaterialDialog(
                    dialogState = alertDialogState,
                    buttons = {
                        positiveButton("OK")
                    }
                ) {
                    title(text = "Goal(s) overdued")
                    message(text = "At least one goal is overdued. Take care of your goals.")
                }
            }
        }
    }
}
