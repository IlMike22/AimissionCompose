package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.landing_page.use_case.ILandingPageUseCase
import com.example.aimissionlite.models.domain.Genre
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Priority
import com.example.aimissionlite.models.domain.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val useCase: ILandingPageUseCase
) : ViewModel() {
    var state by mutableStateOf(LandingPageState())

    var isDeleteAllGoals: LiveData<Boolean>? = null

//    val allGoals: Flow<List<Goal>> = useCase.getAllGoals()
    private var lastDeletedGoal: Goal = Goal.EMPTY

    val uiEvent = MutableSharedFlow<LandingPageUiEvent>()

    init {
        isDeleteAllGoals = useCase.getDeleteGoalsOnStartup().asLiveData()
        getGoals()
    }

    fun onEvent(event: LandingPageUiEvent) {
        when (event) {
            is LandingPageUiEvent.OnAddGoalClicked -> {
                onAddGoalClicked()
            }
            is LandingPageUiEvent.NavigateToDetailGoal -> {
                println("!! navigate to detail goal")
                onGoalContainerClicked(event.goal)
            }
            is LandingPageUiEvent.NavigateToInfo -> {}
            is LandingPageUiEvent.NavigateToSettings -> {
                onSettingsClicked()
            }
        }
    }

    fun onAddGoalClicked() {
        viewModelScope.launch {
            //TODO remove again - just for testing.
            state = state.copy(
                goals = listOf(
                    Goal(0,"first item!","first description","2022-10-31","",false, Genre.BUSINESS,Status.TODO,Priority.LOW),
                    Goal(1,"second item!","second description","2022-10-22","",false, Genre.FITNESS,Status.TODO,Priority.NORMAL),
                    Goal(2,"third item!","third description","2022-10-01","",true, Genre.PARTNERSHIP,Status.TODO,Priority.HIGH),
                    Goal(3,"fourth item!","fourth description","2022-10-31","",false, Genre.MONEY,Status.TODO,Priority.LOW),
                    Goal(4,"fifth item!","fifth description","2022-10-22","",false, Genre.SOCIALISING,Status.TODO,Priority.NORMAL),
                    Goal(5,"six item!","six description","2022-10-01","",true, Genre.HEALTH,Status.TODO,Priority.HIGH)
                )
            )
            uiEvent.emit(LandingPageUiEvent.NavigateToDetailGoal())
        }
    }

    fun onInfoClicked() {
        viewModelScope.launch {
            uiEvent.emit(LandingPageUiEvent.NavigateToInfo)
        }
    }

    fun onSettingsClicked() {
        viewModelScope.launch {
            uiEvent.emit(LandingPageUiEvent.NavigateToSettings)
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
                uiEvent.emit(LandingPageUiEvent.NavigateToDetailGoal(this))
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

    private fun getGoals() {
        viewModelScope.launch {
            useCase.getAllGoals().collect { goals ->
                state = state.copy(
                    goals = goals
                )
            }

        }
    }

    private fun getNewGoalStatus(oldStatus: Status): Status =
        when (oldStatus) {
            Status.DONE -> Status.TODO
            Status.TODO -> Status.IN_PROGRESS
            Status.IN_PROGRESS -> Status.DONE
            else -> Status.UNKNOWN
        }
}