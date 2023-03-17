package com.mind.market.aimissioncompose.presentation.settings

data class SettingsState(
    val isShowSnackbar: Boolean = false,
    val isDoneGoalsHidden: Boolean = false,
    val showGoalOverdueDialogOnStart: Boolean = false,
    val snackbarMessage: String? = null
)

