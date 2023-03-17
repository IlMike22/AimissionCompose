package com.mind.market.aimissioncompose.presentation.information

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InformationScreen(
    state: InformationState,
    onEvent: (InformationEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        state.information.forEach { infoValue ->
            Text(
                text = infoValue
            )
            Spacer(modifier = modifier.height(16.dp))
        }

        Button(
            onClick = {
                onEvent(InformationEvent.OnChangeVersion)
            }) {
            Text(text = "Change version")
        }
    }
}