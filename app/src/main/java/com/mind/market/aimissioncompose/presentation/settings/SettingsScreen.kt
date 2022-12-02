package com.mind.market.aimissioncompose.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val state = viewModel.settingsState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Your personal settings")
        Spacer(modifier = modifier.height(8.dp))
        Button(onClick = {
            viewModel.onEvent(SettingsEvent.DuplicateGoals)
        }) {
            Text(text = "Duplicate the goals!")
        }
        Spacer(modifier = modifier.height(16.dp))
        Text(text = state.duplicateGoalsMessage)
    }
}