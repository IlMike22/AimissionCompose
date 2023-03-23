package com.mind.market.aimissioncompose.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mind.market.aimissioncompose.auth.presentation.AuthenticationEvent

@Composable
fun ClearIcon(onEvent: (AuthenticationEvent) -> Unit) { // TODO MIC make it generic
    Icon(
        imageVector = Icons.Default.Clear,
        contentDescription = "clear text",
        modifier = Modifier
            .clickable { onEvent(AuthenticationEvent.OnClearEmailText) }
            .padding(8.dp)
    )
}