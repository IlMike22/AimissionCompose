package com.mind.market.aimissioncompose.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.IStatisticsRepository
import com.mind.market.aimissioncompose.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: ISettingsUseCase,
    private val statisticsRepository: IStatisticsRepository
) : ViewModel() {
    private val TAG = SettingsViewModel::class.java.toString()
    private val isDeleteGoalOnStartup = MutableLiveData<Resource<Flow<Boolean>>>()

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

            statisticsRepository.getAmountGoalsForStatus(
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        println("!! lading state")
                    }
                    is Resource.Error -> {
                        println("failed state")
                    }
                    is Resource.Success -> {
                        settingsState = settingsState.copy(
                            goalsCompleted = result.data?.get(Status.DONE) ?: -1,
                            goalsTodo = result.data?.get(Status.TODO) ?: -1,
                            goalsInProgress = result.data?.get(Status.IN_PROGRESS) ?: -1
                        )
                    }
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

//    fun onDeleteGoalsClicked(isEnabled: Boolean) {
//        viewModelScope.launch {
//            useCase.setDeleteGoalsOnStartup(isEnabled)
//        }
//    }

    fun getHeaderText() = useCase.getHeaderText()
}

