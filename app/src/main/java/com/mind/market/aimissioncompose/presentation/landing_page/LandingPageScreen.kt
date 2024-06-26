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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import com.mind.market.aimissioncompose.presentation.landing_page.components.Goal
import com.mind.market.aimissioncompose.presentation.utils.Converters.toText
import com.mind.market.aimissioncompose.presentation.utils.SortingMode
import com.mind.market.aimissioncompose.ui.theme.DarkestBlue
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LandingPageScreen(
    state: LandingPageUiState,
    modifier: Modifier = Modifier,
    uiEvent: Flow<LandingPageUiEvent>,
    commandProcessor: (ICommand) -> Unit,
    onShowFeedbackDialog: () -> Unit,
    navController: NavController,
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val alertDialogState = rememberMaterialDialogState()
    val focusRequester = remember { FocusRequester() }

    val detailPageScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("invalidate")?.observeAsState()

    handleBackNavigationUpdate(navController, detailPageScreenResult, commandProcessor)

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

    LaunchedEffect(true) {
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
                                    commandProcessor(UndoDeletedGoalCommand())
                                }

                                else -> {}
                            }
                        }

                        SnackbarResult.Dismissed -> Unit
                    }
                }

                is LandingPageUiEvent.ShowGoalOverdueDialog -> {
                    alertDialogState.show()
                }

                is LandingPageUiEvent.NavigateToAuthenticationScreen -> {
                    navController.navigate(Route.AUTHENTICATION) {
                        popUpTo(0)
                    }
                }

                is LandingPageUiEvent.NavigateToDetailGoal -> Unit
                LandingPageUiEvent.NavigateToStocksDiaryOverviewScreen -> {
                    navController.navigate(Route.STOCKS_DIARY_OVERVIEW)
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
                    ) { Text(text = stringResource(R.string.bottom_sheet_button_text_statistics)) }

//                    Button(
//                        modifier = Modifier.align(Center),
//                        onClick = { onShowFeedbackDialog() }
//                    ) { Text(text = stringResource(R.string.bottom_sheet_button_text_review)) }

                    Button(
                        modifier = Modifier.align(Center),
                        onClick = { commandProcessor(NavigateToStocksDiaryOverviewCommand()) }
                    ) {
                        Text(text = "Stocks Diary")
                    }

                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        onClick = { commandProcessor(LogoutCommand()) }
                    ) { Text(text = stringResource(R.string.bottom_sheet_button_text_logout)) }
                }
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.bottom_sheet_label_text_review),
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
                if (state.hasResults.not() && state.isLoading.not()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Center)
                        ) {// EMPTY SCREEN
                            Text(
                                text = "No goals yet",
                                color = DarkestBlue,
                                style = MaterialTheme.typography.h4
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "It`s very empty here. Start to create some goals.",
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Column(modifier = Modifier.align(CenterHorizontally)) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = "empty screen image"
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { commandProcessor(AddCommand()) },
                                ) {
                                    Text(text = "Create goal")
                                }
                            }
                        }
                    }
                } else if (state.errorMessage != null) { // ERROR CASE
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Center),
                            text = "${state.errorMessage}",
                            style = MaterialTheme.typography.h6,
                            color = Color.DarkGray
                        )
                        Button(
                            modifier = Modifier
                                .align(Alignment.BottomCenter),
                            onClick = {
                                commandProcessor(ResetErrorStateCommand())
                            }
                        ) {
                            Text(stringResource(R.string.landing_page_error_button_text_I_understand))
                        }
                    }

                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester)
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textStyle = TextStyle(color = Color.Black),
                            value = state.searchText,
                            trailingIcon = {
                                if (state.searchText.isNotBlank()) ClearIcon(
                                    commandProcessor
                                )
                            },
                            onValueChange = {
                                commandProcessor(SearchTextUpdateCommand(it))
                            },
                            placeholder = {
                                if (state.searchText.isBlank()) Text(text = "Search for a goal")
                            }
                        )
                        Box {
                            IconButton(
                                onClick = { commandProcessor(DropDownStateChangeCommand(true)) },
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        tint = DarkestBlue,
                                        contentDescription = "sort"
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = state.isDropDownExpanded,
                                onDismissRequest = {
                                    commandProcessor(
                                        DropDownStateChangeCommand(false)
                                    )
                                }
                            ) {
                                SortingMode.values().forEach {
                                    DropdownMenuItem(onClick = {
                                        commandProcessor(SortingChangeCommand(it))
                                    }) {
                                        Text(
                                            text = it.toText(context),
                                            fontWeight = if (state.selectedSortingMode == it) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                if (state.isLoading) { // LOADING SCREEN
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(TopCenter)
                                .padding(top = 24.dp)
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {// SUCCESS SCREEN
                        Column {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                items(state.goalItems) { goalListItem ->
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "${goalListItem.monthValue} ${goalListItem.yearValue}",
                                            fontSize = 14.sp,
                                            color = DarkestBlue,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                        goalListItem.goals.forEach { goal ->
                                            Goal(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxSize()
                                                    .padding(8.dp),
                                                onClicked = {
                                                    navController.navigate(Route.ADD + "?goalId=${it.id}")
                                                },
                                                goal = goal,
                                                onDeleteClicked = {
                                                    commandProcessor(DeleteCommand(it))
                                                },
                                                onStatusChangeClicked = { selectedGoal ->
                                                    commandProcessor(
                                                        StatusChangeCommand(
                                                            selectedGoal
                                                        )
                                                    )
                                                },
                                                dropDownItems = state.dropDownItems,
                                                onDropDownItemClicked = {
                                                    commandProcessor(
                                                        ShowDropDownListCommand(it)
                                                    )
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Divider(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        FloatingActionButton(
                            modifier = Modifier
                                .align(BottomEnd)
                                .padding(bottom = 24.dp),
                            onClick = { navController.navigate(Route.ADD) }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add goal"
                            )
                        }
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

@Composable
private fun ClearIcon(processCommand: (ICommand) -> Unit) {
    Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = "clear text",
        modifier = Modifier
            .clickable { processCommand(ClearSearchTextCommand()) }
            .padding(8.dp)
    )
}

private fun handleBackNavigationUpdate(
    navController: NavController,
    result: State<Boolean?>?,
    commandProcessor: (ICommand) -> Unit
) {
    result?.value?.let { isUpdateList ->
        if (isUpdateList) {
            commandProcessor(GoalUpdateCommand())
            navController.currentBackStackEntry?.savedStateHandle?.set("invalidate", false)
        }
    }
}
