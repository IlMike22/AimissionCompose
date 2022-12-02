package com.mind.market.aimissioncompose.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state = viewModel.settingsState
    val scaffoldState = rememberScaffoldState()

    if (state.isShowSnackbar) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = state.snackBarDuplicateGoalsSuccessMessage
            )
        }
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState
    ) {
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

}