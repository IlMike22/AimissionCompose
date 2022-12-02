package com.mind.market.aimissioncompose.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.mind.market.aimissioncompose.core.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: ISettingsUseCase
) : ViewModel() {
    private val isDeleteGoalOnStartup = MutableLiveData<Resource<Flow<Boolean>>>()

    var settingsState by mutableStateOf(SettingsState())
    private set

    init {
        val result = useCase.getDeleteGoalsOnStartup()
        isDeleteGoalOnStartup.postValue(Resource.Success(result))
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
        }
    }

    fun onDeleteGoalsClicked(isEnabled: Boolean) {
        viewModelScope.launch {
            useCase.setDeleteGoalsOnStartup(isEnabled)
        }
    }

    fun getHeaderText() = useCase.getHeaderText()
}

