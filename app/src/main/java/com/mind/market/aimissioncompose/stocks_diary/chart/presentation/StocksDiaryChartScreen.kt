package com.mind.market.aimissioncompose.stocks_diary.chart.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StocksDiaryChartScreen(
    state: StocksDiaryChartState,
    navController: NavController,
    onEvent: (StocksDiaryChartEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Text(text = "Hello Chart View!", color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = state.monthName, color = Color.Black)
    }

}