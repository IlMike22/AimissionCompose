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
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val repository: IGoalRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val TAG = LandingPageViewModel.javaClass.toString()
    var state by mutableStateOf(LandingPageState())
        private set

    var isDeleteAllGoals: LiveData<Boolean>? = null
    private lateinit var deletedGoal: Goal
    private var deletedGoalIndex = -1
    private var getGoalsJob: Job? = null

    //    val allGoals: Flow<List<Goal>> = useCase.getAllGoals()
    private val _uiEvent = Channel<LandingPageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getGoals()
    }

    fun onEvent(event: LandingPageUiEvent) {
        when (event) {
            is LandingPageUiEvent.OnAddGoalClicked -> {
                navigateToAddGoalScreen()
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
            is LandingPageUiEvent.OnUndoDeleteGoalClicked -> {
                restoreDeletedGoal()
            }
            is LandingPageUiEvent.OnListUpdated -> {
            }
        }
    }

    private fun navigateToAddGoalScreen() {
        viewModelScope.launch {
            _uiEvent.send(LandingPageUiEvent.NavigateToDetailGoal())
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
                getGoals()
            }
        }
    }

    private fun onGoalContainerClicked(goal: Goal?) {
        viewModelScope.launch {
            goal?.apply {
                _uiEvent.send(LandingPageUiEvent.NavigateToDetailGoal(this))
            }
        }
    }

    private fun deleteGoal(goal: Goal?) {
        deletedGoal = goal ?: Goal.EMPTY
        deletedGoalIndex = state.goals.indexOf(deletedGoal)
        deletedGoal.apply {
            viewModelScope.launch {
                try {
                    repository.deleteGoal(deletedGoal)
                    getGoals()
                    _uiEvent.send(
                        LandingPageUiEvent.ShowSnackbar(
                            message = "The goal was successfully deleted.",
                            snackbarAction = SnackBarAction.UNDO
                        )
                    )
                } catch (exception: java.lang.Exception) {
                    _uiEvent.send(LandingPageUiEvent.ShowSnackbar(message = "The goal could not be deleted. Try again."))
                }
            }
        }
    }


    private fun restoreDeletedGoal() {
        if (deletedGoal != Goal.EMPTY) {
            viewModelScope.launch {
                repository.insert(deletedGoal)
                state = state.copy(
                    goals = state.goals + deletedGoal
                )
            }
        }
    }

    suspend fun deleteAllGoals(): Boolean {
        return repository.deleteAll()
    }

    fun getGoals() {
        getGoalsJob?.cancel()
        getGoalsJob = viewModelScope.launch {
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
                            isLoading = response.isLoading
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