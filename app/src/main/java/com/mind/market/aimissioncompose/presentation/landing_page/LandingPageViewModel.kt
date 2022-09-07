package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.domain.models.Goal
import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val repository: IGoalRepository
) : ViewModel() {
    var state by mutableStateOf(LandingPageState())

    var isDeleteAllGoals: LiveData<Boolean>? = null

//    val allGoals: Flow<List<Goal>> = useCase.getAllGoals()
    private var lastDeletedGoal: Goal = Goal.EMPTY

    val uiEvent = MutableSharedFlow<LandingPageUiEvent>()

    init {
//        isDeleteAllGoals = repository.getDeleteGoalsOnStartup().asLiveData()
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
            is LandingPageUiEvent.OnDeleteGoalClicked -> TODO()
            is LandingPageUiEvent.OnStatusChangedClicked -> TODO()
            is LandingPageUiEvent.ShowSnackbar -> TODO()
        }
    }

    private fun onAddGoalClicked() {
        viewModelScope.launch {
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
                repository.updateStatus(
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
                val isDeleteSucceeded = repository.deleteGoal(goal)
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
                repository.insert(lastDeletedGoal)
            }
        }
    }

    suspend fun deleteAllGoals(): Boolean {
        return repository.deleteAll()
    }

    private fun getGoals() {
        viewModelScope.launch {
            repository.getGoals().collect { response ->
                when(response) {
                    is Resource.Success -> {
                        state = state.copy(
                            goals = response.data?: emptyList(),
                            isLoading = false,
                            errorMessage = ""
                        )
                    }
                    is Resource.Loading -> {
                        state = state.copy(
                            goals = emptyList(),
                            isLoading = true,
                            errorMessage = ""
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            goals = emptyList(),
                            isLoading = false,
                            errorMessage = response.message?:"Unknown error while loading data."
                        )
                    }
                }
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