package com.mind.market.aimissioncompose.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.domain.settings.use_case.GetUserSettingsUseCase
import com.mind.market.aimissioncompose.domain.settings.use_case.HideDoneGoalsUseCase
import com.mind.market.aimissioncompose.domain.settings.use_case.ShowOverdueDialogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserSettings: GetUserSettingsUseCase,
    private val hideDoneGoals: HideDoneGoalsUseCase,
    private val showOverdueDialog: ShowOverdueDialogUseCase,
) : ViewModel() {
    var settingsState by mutableStateOf(SettingsState())
        private set

    init {
        viewModelScope.launch {
            launch {
                getUserSettings().collect { userSettings ->
                    settingsState = settingsState.copy(
                        isDoneGoalsHidden = userSettings.isHideDoneGoals,
                        showGoalOverdueDialogOnStart = userSettings.showGoalOverdueDialog
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.HideDoneGoals -> {
                viewModelScope.launch {
                    hideDoneGoals(event.hide)
                    settingsState = settingsState.copy(
                        isDoneGoalsHidden = event.hide
                    )
                }
            }
            is SettingsEvent.ShowGoalOverdueDialog -> {
                viewModelScope.launch {
                    showOverdueDialog(event.show)
                    settingsState = settingsState.copy(
                        showGoalOverdueDialogOnStart = event.show
                    )
                }
            }
        }
    }
}

