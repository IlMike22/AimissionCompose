package com.mind.market.aimissioncompose.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mind.market.aimissioncompose.presentation.detail.UserNotAuthenticatedScreen
import com.mind.market.aimissioncompose.ui.theme.DarkBlue
import com.mind.market.aimissioncompose.ui.theme.DarkestBlue

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
                message = state.snackbarMessage ?: "Unknown message"
            )
        }
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState
    ) {
        if (!state.isUserAuthenticated) {
            UserNotAuthenticatedScreen(modifier = modifier)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your personal settings",
                    style = MaterialTheme.typography.h6,
                    color = DarkestBlue
                )
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
                    Text(text = "Hide successfully done goals in list", color = DarkBlue)
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
                    Text(text = "Show goal overdue dialog on startup", color = DarkBlue)
                }
            }
        }
    }
}