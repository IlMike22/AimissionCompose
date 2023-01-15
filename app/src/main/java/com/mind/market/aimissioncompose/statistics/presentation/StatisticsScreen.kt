package com.mind.market.aimissioncompose.statistics.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.R

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
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colors.primary,
                strokeWidth = 3.dp
            )
        } else {
            val text =
                if (state.errorMessage.isNullOrBlank()) stringResource(id = R.string.hello_success)
                else stringResource(id = R.string.hello_error)
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2
            )
        }
    }
}