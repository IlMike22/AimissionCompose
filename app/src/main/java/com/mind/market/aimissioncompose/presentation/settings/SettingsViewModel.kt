package com.mind.market.aimissioncompose.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.ICommonStatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: ISettingsUseCase,
) : ViewModel() {
    private val TAG = SettingsViewModel::class.java.toString()

    var settingsState by mutableStateOf(SettingsState())
        private set

    init {
        viewModelScope.launch {
            launch {
                useCase.getUserSettings().collect { userSettings ->
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
            SettingsEvent.DuplicateGoals -> {
                viewModelScope.launch {
                    val isSuccess = useCase.duplicateGoals()
                    settingsState = if (isSuccess) {
                        settingsState.copy(
                            duplicateGoalsMessage = "Goals were successfully duplicated!",
                            snackBarDuplicateGoalsSuccessMessage = "Goals successfully duplicated!",
                            isShowSnackbar = true
                        )
                    } else {
                        settingsState.copy(
                            duplicateGoalsMessage = "Error while duplicating goals.",
                            snackBarDuplicateGoalsSuccessMessage = "Goal duplication failed!",
                            isShowSnackbar = true
                        )
                    }
                }
            }
            is SettingsEvent.HideDoneGoals -> {
                viewModelScope.launch {
                    useCase.setHideDoneGoals(event.hide)
                    settingsState = settingsState.copy(
                        isDoneGoalsHidden = event.hide
                    )
                }
            }
            is SettingsEvent.ShowGoalOverdueDialog -> {
                viewModelScope.launch {
                    useCase.setShowGoalOverdueDialog(event.show)
                    settingsState = settingsState.copy(
                        showGoalOverdueDialogOnStart = event.show
                    )
                }
            }
        }
    }
}

