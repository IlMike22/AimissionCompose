package com.mind.market.aimissioncompose.stocks_diary.detail.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.StocksDiaryDetailEvent
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.StocksDiaryDetailState

@Composable
fun StocksTradingScreen(
    modifier: Modifier = Modifier,
    state: StocksDiaryDetailState,
    onEvent: (StocksDiaryDetailEvent) -> Unit
) {

    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = { isDialogOpen = true })
        {
            Text(text = "Add new item", modifier = Modifier.padding(8.dp))
        }

        if (isDialogOpen) {
            StocksTradingItemCreateDialog(
                onEvent = onEvent,
                onDismiss = { isDialogOpen = false }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Column {
            state.stocksDiary.stocksTraded.forEach {
                Box {
                    Text(text = it.name, color = Color.Black)
                }
            }
        }
    }
}

