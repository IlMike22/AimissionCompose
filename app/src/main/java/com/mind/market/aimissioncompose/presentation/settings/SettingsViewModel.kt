package com.mind.market.aimissioncompose.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.domain.settings.use_case.GetUserSettingsUseCase
import com.mind.market.aimissioncompose.domain.settings.use_case.HideDoneGoalsUseCase
import com.mind.market.aimissioncompose.domain.settings.use_case.ShowOverdueDialogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserSettings: GetUserSettingsUseCase,
    private val hideDoneGoals: HideDoneGoalsUseCase,
    private val showOverdueDialog: ShowOverdueDialogUseCase,
) : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = _settingsState.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth

    init {
        viewModelScope.launch {
            launch {
                getUserSettings().collect { userSettings ->
                    _settingsState.update {
                        it.copy(
                            isDoneGoalsHidden = userSettings.isHideDoneGoals,
                            showGoalOverdueDialogOnStart = userSettings.showGoalOverdueDialog,
                            isUserAuthenticated = auth.currentUser != null
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.HideDoneGoals -> {
                viewModelScope.launch {
                    hideDoneGoals(event.hide)
                    _settingsState.update { it.copy(isDoneGoalsHidden = event.hide) }
                }
            }

            is SettingsEvent.ShowGoalOverdueDialog -> {
                viewModelScope.launch {
                    showOverdueDialog(event.show)
                    _settingsState.update { it.copy(showGoalOverdueDialogOnStart = event.show) }
                }
            }
        }
    }
}

