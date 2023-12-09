package com.mind.market.aimissioncompose.stocks_diary.overview.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.navigation.Route
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class, ExperimentalUnitApi::class)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.errorMessage != null) {
            Text(text = state.errorMessage)
        } else {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                itemsIndexed(state.stockDiaries) { index,item ->
                    val dismissState = rememberDismissState()

                    //TODO MIC current problem here. all items below current item are removed !!!
                    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                        onEvent(StocksDiaryOverviewEvent.OnItemRemove(item))
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier.padding(vertical = Dp(1f)),
                        directions = setOf(DismissDirection.EndToStart),
                        dismissThresholds = { direction ->
                            FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                        },
                        background = {
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    DismissValue.Default -> Color.White
                                    else -> Color.Red
                                }
                            )
                            val alignment = Alignment.CenterEnd
                            val icon = Icons.Default.Delete

                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(horizontal = Dp(20f)),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Delete Icon",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {
                            Card(
                                elevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                                ).value,
                                modifier = Modifier
                                    .height(140.dp)
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .align(alignment = Alignment.CenterVertically)
                            ) {
                                StocksDiaryItem(item)
                            }
                        }
                    )
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

@ExperimentalUnitApi
@Composable
fun SetUpRow(
    item: StocksDiaryDomain
) {

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