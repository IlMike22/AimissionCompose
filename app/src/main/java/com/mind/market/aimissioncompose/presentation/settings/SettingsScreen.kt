package com.mind.market.aimissioncompose.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
            Spacer(modifier = modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.isDoneGoalsHidden,
                    onCheckedChange = { isChecked ->
                        viewModel.onEvent(SettingsEvent.HideDoneGoals(isChecked))
                    })

                Spacer(modifier = modifier.width(8.dp))

                Text(text = "Hide successfully done goals in list")
            }

            Spacer(modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.showGoalOverdueDialogOnStart,
                    onCheckedChange = { isChecked ->
                        viewModel.onEvent(SettingsEvent.ShowGoalOverdueDialog(isChecked))
                    })

                Spacer(modifier = modifier.width(8.dp))
                Text(text = "Show goal overdue dialog on startup")
            }

            Spacer(modifier.height(16.dp))
            Text(text = "Full amount of completed goals is ${state.goalsCompleted}")
            Text(text = "Full amount of open goals is ${state.goalsTodo}")
            Text(text = "Full amount of goals in progress is ${state.goalsInProgress}")
        }
    }

}