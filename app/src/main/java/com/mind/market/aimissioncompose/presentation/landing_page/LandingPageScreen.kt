package com.mind.market.aimissioncompose.presentation.landing_page

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.R
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
    onShowFeedbackDialog: () -> Unit,
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
                        .padding(24.dp)
                        .height(250.dp),
                    contentAlignment = Center
                ) {
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        onClick = {
                            navController.navigate(Route.STATISTICS)
                        }
                    ) { Text(text = "Statistic") }

                    Button(
                        modifier = Modifier.align(Center),
                        onClick = { onShowFeedbackDialog() }
                    ) { Text(text = "Review") }

                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        onClick = { onEvent(LandingPageUiEvent.OnLogoutUserClicked) }
                    ) { Text(text = "Logout") }
                }
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Review dialog only works when app is in Google Play.",
                    color = Color.White
                )
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
                } else if (state.goals.isEmpty()) {
                    // show empty screen
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Center)
                        ) {
                            Text(
                                text = "No goals yet",
                                style = MaterialTheme.typography.h4
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "It`s very empty here. Start to create some goals.")
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(modifier = Modifier.align(CenterHorizontally)) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = "empty screen image"
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { onEvent(LandingPageUiEvent.OnAddGoalClicked) },
                                ) {
                                    Text(text = "Create goal")
                                }
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column {
                            OutlinedTextField(
                                value = state.searchText,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                onValueChange = {
                                    onEvent(LandingPageUiEvent.OnSearchTextUpdate(it))
                                },
                                placeholder = {
                                    if (state.searchText.isBlank()) Text(text = "Search for a goal")
                                }
                            )
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
                                            onEvent(
                                                LandingPageUiEvent.OnDeleteGoalClicked(
                                                    goalToDelete
                                                )
                                            )
                                        },
                                        onStatusChangeClicked = { selectedGoal ->
                                            onEvent(
                                                LandingPageUiEvent.OnStatusChangedClicked(
                                                    selectedGoal
                                                )
                                            )
                                        }
                                    )

                                    Divider(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                        FloatingActionButton(
                            modifier = Modifier
                                .align(BottomEnd)
                                .padding(bottom = 24.dp),
                            onClick = { navController.navigate(Route.ADD) }
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add goal")
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
