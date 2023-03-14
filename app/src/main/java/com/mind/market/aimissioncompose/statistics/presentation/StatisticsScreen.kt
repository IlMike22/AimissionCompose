package com.mind.market.aimissioncompose.statistics.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mind.market.aimissioncompose.statistics.presentation.components.StatisticsItem

@Composable
fun StatisticsScreen(
    state: StatisticsState,
    onEvent: (StatisticsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colors.primary,
                strokeWidth = 3.dp
            )
        } else if (state.errorMessage.isNullOrBlank()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                item {
                    Text(
                        text = "Your statistics overview",
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(thickness = 2.dp)
                }

                items(state.statisticsEntities) { entity ->
                    StatisticsItem(item = entity)
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            val text = state.errorMessage
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1
            )
        }
    }
}