package com.mind.market.aimissioncompose.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scaffoldState = rememberScaffoldState()

    if (state.isShowSnackbar) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = state.snackbarMessage?:"Unknown message"
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
            Text(text = "Your personal settings", fontStyle = FontStyle.Normal)
            Spacer(modifier = modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.isDoneGoalsHidden,
                    onCheckedChange = { isChecked ->
                        onEvent(SettingsEvent.HideDoneGoals(isChecked))
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
                        onEvent(SettingsEvent.ShowGoalOverdueDialog(isChecked))
                    })

                Spacer(modifier = modifier.width(8.dp))
                Text(text = "Show goal overdue dialog on startup")
            }
        }
    }
}