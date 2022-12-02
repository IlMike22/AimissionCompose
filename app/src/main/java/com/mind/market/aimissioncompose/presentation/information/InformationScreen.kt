package com.mind.market.aimissioncompose.presentation.information

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun InformationScreen(
    viewModel: InformationViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val informationState = viewModel.informationFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        informationState.value.data?.forEach { infoValue ->
            Text(
                text = infoValue
            )
            Spacer(modifier = modifier.height(16.dp))
        }

        Button(
            onClick = {
                viewModel.changeVersion()
            }) {
            Text(text = "Change version")
        }
    }
}