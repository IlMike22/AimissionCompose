package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.landing_page.use_case.DeleteGoalUseCase
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val useCase: ILandingPageUseCase,
    private val deleteGoal: DeleteGoalUseCase,
    private val settingsUseCase: ISettingsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LandingPageState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        LandingPageState()
    )

    //var state by mutableStateOf(LandingPageState())
    //  private set

    var isDeleteAllGoals: LiveData<Boolean>? = null
    private var isDoneGoalHidden = false
    private var showGoalOverdueDialog = false
    private lateinit var deletedGoal: Goal
    private var goalIndex = -1
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
                cacheAndDeleteGoal(event.goal ?: Goal.EMPTY)
            }
            is LandingPageUiEvent.OnStatusChangedClicked -> {
                updateGoalStatus(event.goal)
            }
            is LandingPageUiEvent.OnUndoDeleteGoalClicked -> {
                restoreDeletedGoal()
            }
            is LandingPageUiEvent.OnGoalUpdate -> {
                viewModelScope.launch {
                    getGoals()
                }
            }
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

    private fun cacheAndDeleteGoal(goal: Goal) {
        deletedGoal = goal // TODO MIC maybe extract this logic into the usecase
        goalIndex = state.value.goals.indexOf(goal)
        goal.apply {
            viewModelScope.launch {
                try {
                    deleteGoal(goal)
                    deletedGoal = goal
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
                _state.update {
                    it.copy(
                        goals = state.value.goals + deletedGoal
                    )
                }
            }
        }
    }

    suspend fun getGoals() {
        useCase.getGoals(GoalReadWriteOperation.FIREBASE_DATABASE).collect { response ->
            when (response) {
                is Resource.Success -> {
                    val goals = response.data ?: emptyList()
                    _state.update {
                        it.copy(
                            goals = goals,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    showGoalOverdueDialogIfNeeded(goals)
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isLoading = response.isLoading
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            goals = emptyList(),
                            isLoading = false,
                            errorMessage = response.message ?: "Unknown error while loading data."
                        )
                    }
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