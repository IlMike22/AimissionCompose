package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.landing_page.use_case.ILandingPageUseCase
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val useCase: ILandingPageUseCase
) : ViewModel() {
    var isDeleteAllGoals: LiveData<Boolean>? = null

    val allGoals: Flow<List<Goal>> = useCase.getAllGoals()
    private var lastDeletedGoal: Goal = Goal.EMPTY

    val uiEvent = MutableSharedFlow<LandingPageUiEvent>()

    init {
        isDeleteAllGoals = useCase.getDeleteGoalsOnStartup().asLiveData()
    }

    fun onAddGoalClicked() {
        viewModelScope.launch {
            uiEvent.emit(LandingPageUiEvent.NavigateToDetail())
        }
    }

    fun onInfoClicked() {
        viewModelScope.launch {
            uiEvent.emit(LandingPageUiEvent.NavigateToInfo())
        }
    }

    fun onSettingsClicked() {
        viewModelScope.launch {
            uiEvent.emit(LandingPageUiEvent.NavigateToSettings())
        }
    }


    fun onGoalStatusClicked(goal: Goal?) {
        goal?.apply {
            viewModelScope.launch {
                useCase.updateGoalStatus(
                    id = goal.id,
                    status = getNewGoalStatus(goal.status)
                )
            }
        } ?: println("!!! Goal is null. Cannot update goal status.")
    }

    fun onGoalContainerClicked(goal: Goal?) {
        viewModelScope.launch {
            goal?.apply {
                uiEvent.emit(LandingPageUiEvent.NavigateToDetail(this))
            }
        }
    }

    fun onGoalDeleteClicked(goal: Goal?) {
        lastDeletedGoal = goal ?: Goal.EMPTY
        goal?.apply {
            viewModelScope.launch {
                val isDeleteSucceeded = useCase.deleteGoal(goal)
                if (isDeleteSucceeded.not()) {
                    println("!!! Error while deleting the goal.")
                }

                uiEvent.emit(LandingPageUiEvent.ShowSnackbar("Goal deleted."))
            }
        } ?: println("!!! Goal is null. Cannot delete goal.")
    }

    fun restoreDeletedGoal() {
        if (lastDeletedGoal != Goal.EMPTY) {
            viewModelScope.launch {
                useCase.insertGoal(lastDeletedGoal)
            }
        }
    }

    suspend fun deleteAllGoals(): Boolean {
        return useCase.deleteAllGoals()
    }

    private fun getNewGoalStatus(oldStatus: Status): Status =
        when (oldStatus) {
            Status.DONE -> Status.TODO
            Status.TODO -> Status.IN_PROGRESS
            Status.IN_PROGRESS -> Status.DONE
            else -> Status.UNKNOWN
        }
}