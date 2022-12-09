package com.mind.market.aimissioncompose.presentation.settings

data class SettingsState(
    val duplicateGoalsMessage: String = "",
    val snackBarDuplicateGoalsSuccessMessage: String = "",
    val isShowSnackbar: Boolean = false,
    val isDoneGoalsHidden: Boolean = false
)

