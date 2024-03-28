package com.mind.market.aimissioncompose.stocks_diary.overview.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StocksDiaryOverviewScreen(
    navController: NavController,
    state: StocksDiaryOverviewState,
    onEvent: (StocksDiaryOverviewEvent) -> Unit
) {
    val detailPageScreenResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("invalidate")?.observeAsState()

    handleBackNavigationUpdate(
        navController,
        detailPageScreenResult,
        onEvent
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }


    LaunchedEffect(key1 = state.showUndoSnackbar) {
        if (state.showUndoSnackbar) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Item deleted. Do you want to revert this?",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Long
                )

                when (result) {
                    SnackbarResult.Dismissed -> onEvent(StocksDiaryOverviewEvent.OnDismissSnackbar)
                    SnackbarResult.ActionPerformed -> onEvent(StocksDiaryOverviewEvent.OnUndoItemRemove)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else if (state.errorMessage != null) {
                Text(text = state.errorMessage)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            navController.navigate(
                                Route.STOCKS_DIARY_CHART
                                        + "?month=${state.currentMonth}&year=${state.currentYear}"
                            )
                        }
                    ) {
                        Text(text = "Open Chart View")
                    }
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(
                            items = state.stockDiaries,
                            key = { it }
                        ) { item ->
                            val alignment = Alignment.CenterEnd
                            val icon = Icons.Default.Delete

                            SwipeToDeleteContainer(
                                item = item,
                                onDelete = {
                                    onEvent(StocksDiaryOverviewEvent.OnItemRemove(item))
                                }) { stocksItem ->
                                Card(
                                    modifier = Modifier
                                        .height(140.dp)
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                ) {
                                    StocksDiaryItem(stocksItem)
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 24.dp),
                onClick = { navController.navigate(Route.STOCKS_DIARY_DETAIL) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add new stock diary entry"
                )
            }
        }
    }
}

@Composable
fun StocksDiaryItem(
    stocksDiary: StocksDiaryDomain
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val pattern = "dd.MM.yyyy"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        Text(
            text = stocksDiary.createdDate.format(formatter),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.align(Alignment.TopStart),
            color = Color.Black
        )
        Text(
            text = stocksDiary.title,
            modifier = Modifier.align(Alignment.CenterStart),
            color = Color.Black
        )
        Text(
            text = stocksDiary.description,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.align(Alignment.BottomStart),
            color = Color.Black
        )
        Icon(
            imageVector = getMoodIcon(stocksDiary.mood),
            contentDescription = "Good mood",
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}


private fun getMoodIcon(mood: Mood): ImageVector =
    when (mood) {
        Mood.GOOD -> Icons.Filled.ThumbUp
        Mood.OKAY -> Icons.Filled.Send
        Mood.BAD -> Icons.Filled.ArrowDropDown
        Mood.UNDEFINED -> Icons.Outlined.Warning
    }

private fun handleBackNavigationUpdate(
    navController: NavController,
    result: State<Boolean?>?,
    onEvent: (StocksDiaryOverviewEvent) -> Unit
) {
    result?.value?.let { isUpdateList ->
        if (isUpdateList) {
            onEvent(StocksDiaryOverviewEvent.OnUpdateList)
            navController.currentBackStackEntry?.savedStateHandle?.set("invalidate", false)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember() {
        mutableStateOf(false)
    }

    val dismissState = rememberDismissState(
        confirmStateChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                DeleteBackground(dismissState)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete, contentDescription = null,
            tint = Color.White
        )
    }
}