package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.landing_page.use_case.GoalOperation
import com.mind.market.aimissioncompose.domain.landing_page.use_case.ILandingPageUseCase
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import com.mind.market.aimissioncompose.statistics.data.dto.Grade
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val useCase: ILandingPageUseCase,
    private val settingsUseCase: ISettingsUseCase,
) : ViewModel() {
    var state by mutableStateOf(LandingPageState())
        private set

    var isDeleteAllGoals: LiveData<Boolean>? = null
    private var isDoneGoalHidden = false
    private var showGoalOverdueDialog = false
    private lateinit var deletedGoal: Goal
    private var deletedGoalIndex = -1
    private var getGoalsJob: Job? = null

    private val _uiEvent = Channel<LandingPageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch {
                settingsUseCase.getUserSettings().collect { userSettings ->
                    isDoneGoalHidden = userSettings.isHideDoneGoals
                    showGoalOverdueDialog = userSettings.showGoalOverdueDialog
                    getGoals()
                }
            }

            launch {
                val currentDate = LocalDateTime.now()
                val currentMonthValue = currentDate.monthValue
                val currentMonth = currentDate.month
                val currentYear = currentDate.year

                if (useCase.doesStatisticExist(currentMonthValue, currentYear)) {
                    useCase.insertStatisticEntity(
                        StatisticsEntity(
                            title = currentMonth.toMonthName(),
                            amountGoalsCompleted = 0,
                            amountGoalsStarted = 0,
                            amountGoalsNotCompleted = 0,
                            amountGoalsCreated = 0,
                            grade = Grade.NO_GOALS_COMPLEDTED,
                            month = currentMonthValue,
                            year = currentYear,
                            created = currentDate,
                            lastUpdated = currentDate
                        )
                    )
                }
            }
        }
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
            is LandingPageUiEvent.ShowGoalOverdueDialog -> TODO()
            is LandingPageUiEvent.ShowSnackbar -> TODO()
            LandingPageUiEvent.OnLogoutUserClicked -> {
                // TODO MIC just for testing logout user. Has to be set into remote ds and
                // probably is not part of the landingpage but in the user settings.
                val auth = Firebase.auth
                auth.signOut()
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
                useCase.executeGoalOperation(
                    GoalOperation.UpdateStatus(
                        id = id,
                        newStatus = newStatus
                    )
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
                    useCase.executeGoalOperation(GoalOperation.Delete(deletedGoal))
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
                useCase.executeGoalOperation(GoalOperation.Insert(deletedGoal))
                state = state.copy(
                    goals = state.goals + deletedGoal
                )
            }
        }
    }

    suspend fun getGoals() {
        useCase.getGoals().collect { response ->
            when (response) {
                is Resource.Success -> {
                    val goals = response.data ?: emptyList()
                    state = state.copy(
                        goals = filterGoals(goals),
                        isLoading = false,
                        errorMessage = ""
                    )
                    showGoalOverdueDialogIfNeeded(goals)
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

    private fun showGoalOverdueDialogIfNeeded(goals: List<Goal>) {
        if (showGoalOverdueDialog.not()) {
            return
        }

        goals.filter { goal ->
            useCase.isGoalOverdue(goal)
        }.apply {
            if (this.isNotEmpty()) {
                viewModelScope.launch {
                    _uiEvent.send(
                        LandingPageUiEvent.ShowGoalOverdueDialog(
                            title = "Goal(s) overdue!",
                            message = "At least one goal is overdue. Take care of your goals."
                        )
                    )
                }
            }
        }
    }

    private fun filterGoals(goals: List<Goal>): List<Goal> {
        return if (isDoneGoalHidden) {
            goals.filter { goal ->
                goal.status != Status.DONE
            }
        } else {
            goals
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

        private fun Month.toMonthName(): String =
            when (this) {
                Month.JANUARY -> "January"
                Month.FEBRUARY -> "February"
                Month.MARCH -> "March"
                Month.APRIL -> "April"
                Month.MAY -> "May"
                Month.JUNE -> "June"
                Month.JULY -> "July"
                Month.AUGUST -> "August"
                Month.SEPTEMBER -> "September"
                Month.OCTOBER -> "October"
                Month.NOVEMBER -> "November"
                Month.DECEMBER -> "December"
            }
    }
}