package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val repository: IGoalRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var state by mutableStateOf(LandingPageState())
        private set

    var isDeleteAllGoals: LiveData<Boolean>? = null

    //    val allGoals: Flow<List<Goal>> = useCase.getAllGoals()
    private var lastDeletedGoal: Goal = Goal.EMPTY

    val uiEvent = MutableSharedFlow<LandingPageUiEvent>()

    init {
        getGoals()
    }

    fun onEvent(event: LandingPageUiEvent) {
        when (event) {
            is LandingPageUiEvent.OnAddGoalClicked -> {
                onAddGoalClicked()
            }
            is LandingPageUiEvent.NavigateToDetailGoal -> {
                onGoalContainerClicked(event.goal)
            }
            is LandingPageUiEvent.OnDeleteGoalClicked -> {
                deleteGoal(event.goal)
            }
            is LandingPageUiEvent.OnStatusChangedClicked -> {
                updateGoalStatus(event.goal)
            }
            is LandingPageUiEvent.ShowSnackbar -> TODO()
        }
    }

    private fun onAddGoalClicked() {
        viewModelScope.launch {
            uiEvent.emit(LandingPageUiEvent.NavigateToDetailGoal())
        }
    }

    private fun updateGoalStatus(goal: Goal?) {
        goal?.apply {
            viewModelScope.launch {
                val newStatus = getNewGoalStatus(goal.status)
                repository.updateStatus(
                    id = id,
                    status = newStatus
                )
                getGoals() // TODO not good to get all goals again after updating them
            }
        }
    }

    private fun onGoalContainerClicked(goal: Goal?) {
        viewModelScope.launch {
            goal?.apply {
                uiEvent.emit(LandingPageUiEvent.NavigateToDetailGoal(this))
            }
        }
    }

    private fun deleteGoal(goal: Goal?): Boolean {
        lastDeletedGoal = goal ?: Goal.EMPTY
        var isDeleteSucceeded = false
        goal?.apply {
            viewModelScope.launch {
                isDeleteSucceeded = repository.deleteGoal(goal)
                state = if (isDeleteSucceeded) {
                    getGoals()

                    state.copy(
                        showSnackbar = true,
                        snackbarMessage = "The goal was successfully deleted."
                    )
                } else {
                    state.copy(
                        showSnackbar = true,
                        snackbarMessage = "The goal could not be deleted. Please try again."
                    )
                }
            }
        } ?: println("!!! Goal is null. Cannot delete goal.")
        return isDeleteSucceeded
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

    fun getGoals() {
        viewModelScope.launch {
            repository.getGoals().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        state = state.copy(
                            goals = response.data ?: emptyList(),
                            isLoading = false,
                            errorMessage = ""
                        )
                    }
                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = response.isLoading,
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            goals = emptyList(),
                            isLoading = false,
                            errorMessage = response.message ?: "Unknown error while loading data."
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

    companion object {
        private const val ARGUMENT_INVALIDATE = "invalidate"
    }
}