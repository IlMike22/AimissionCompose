package com.mind.market.aimissioncompose.presentation.settings

data class SettingsState(
    val duplicateGoalsMessage: String = "",
    val snackBarDuplicateGoalsSuccessMessage: String = "",
    val isShowSnackbar: Boolean = false,
    val isDoneGoalsHidden: Boolean = false,
    val showGoalOverdueDialogOnStart: Boolean = false,
    val goalsCompleted: Int = -1,
    val goalsTodo: Int = -1,
    val goalsInProgress: Int = -1
)

