package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.mind.market.aimissioncompose.auth.domain.LogoutUserUseCase
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.goal.*
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import com.mind.market.aimissioncompose.statistics.data.dto.Grade
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.DoesStatisticExistsUseCase
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.InsertStatisticEntityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val logoutUser: LogoutUserUseCase,
    private val deleteGoal: DeleteGoalUseCase,
    private val insertGoal: InsertGoalUseCase,
    private val getGoals: GetGoalsUseCase,
    private val updateGoalStatus: UpdateGoalStatusUseCase,
    private val isGoalOverdue: IsGoalOverdueUseCase,
    private val settingsUseCase: ISettingsUseCase,
    private val doesStatisticExist: DoesStatisticExistsUseCase,
    private val insertStatisticEntity: InsertStatisticEntityUseCase
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

    private val _uiEvent = Channel<LandingPageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch {
                settingsUseCase.getUserSettings().collect { userSettings ->
                    isDoneGoalHidden = userSettings.isHideDoneGoals
                    showGoalOverdueDialog = userSettings.showGoalOverdueDialog
                    getGoals().collect { handleGoalsResponse(it) }
                }
            }
            val currentDate = LocalDateTime.now()
            val currentMonthValue = currentDate.monthValue
            val currentMonth = currentDate.month
            val currentYear = currentDate.year

            doesStatisticExist(currentMonthValue, currentYear) { doesExist ->
                if (doesExist) {
                    return@doesStatisticExist
                }

                launch {
                    insertStatisticEntity(
                        StatisticsEntity(
                            id = "$currentMonthValue${currentYear}",
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
                        ),
                        onResult = { isSuccess ->
                            if (isSuccess.not()) {
                                viewModelScope.launch {// TODO MIC try to avoid opening a new coroutine here (?)
                                    _uiEvent.send(
                                        LandingPageUiEvent.ShowSnackbar(
                                            message = "Unable to create statistics for current user."
                                        )
                                    ) // TODO MIC reduce callback hell
                                }
                            }
                        }
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
            /*
             We want to avoid calling getGoals() again after status was updated. Therefore first
             the old status is sent to UseCase where it will be transformed into new status. Then new
             status will be stored in firebase and after that was successful we update also the state
             so the ui will be updated as well.
             */
            is LandingPageUiEvent.OnStatusChangedClicked -> {
                event.goal?.apply {
                    viewModelScope.launch {
                        val oldGoals = _state.value.goals
                        val oldGoal = oldGoals.firstOrNull {
                            it.id == this@apply.id
                        }
                        val newGoals = mutableListOf<Goal>()
                        updateGoalStatus(id, status) { newStatus ->
                            oldGoals.forEach {
                                if (it.id == oldGoal?.id) {
                                    newGoals.add(
                                        it.copy(
                                            status = newStatus
                                        )
                                    )
                                } else {
                                    newGoals.add(it)
                                }
                            }
                            _state.update {
                                it.copy(goals = newGoals)
                            }
                        }
                    }
                } ?: _state.update {
                    it.copy(
                        errorMessage = "Cannot update goal because it is invalid."
                    )
                }
            }
            is LandingPageUiEvent.OnUndoDeleteGoalClicked -> {
                restoreDeletedGoal()
            }
            is LandingPageUiEvent.OnGoalUpdate -> {
                viewModelScope.launch {
                    getGoals().collect { handleGoalsResponse(it) }
                }
            }
            is LandingPageUiEvent.OnLogoutUserClicked -> {
                viewModelScope.launch {
                    logoutUser()
                }
            }
        }
    }

    private fun navigateToAddGoalScreen() {
        viewModelScope.launch {
            _uiEvent.send(LandingPageUiEvent.NavigateToDetailGoal())
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
        deletedGoal = goal
        goalIndex = state.value.goals.indexOf(goal)
        goal.apply {
            viewModelScope.launch {
                try {
                    deleteGoal(goal) { isGoalDeleted ->
                        if (isGoalDeleted.not()) {
                            _state.update {
                                it.copy(errorMessage = "Goal could not be deleted. Try again.")
                            }
                            return@deleteGoal
                        }

                        deletedGoal = goal
                        val updatedGoals = mutableListOf<Goal>()
                        _state.value.goals.forEach {
                            if (it != deletedGoal) {
                                updatedGoals.add(it)
                            }
                        }

                        _state.update {
                            it.copy(
                                goals = updatedGoals
                            )
                        }
                        viewModelScope.launch {
                            _uiEvent.send(
                                LandingPageUiEvent.ShowSnackbar(
                                    message = "The goal was successfully deleted.",
                                    snackbarAction = SnackBarAction.UNDO
                                )
                            )
                        }
                    }
                } catch (exception: java.lang.Exception) {
                    _uiEvent.send(LandingPageUiEvent.ShowSnackbar(message = "The goal could not be deleted. Try again."))
                }
            }
        }
    }

    private fun restoreDeletedGoal() {
        if (deletedGoal != Goal.EMPTY) {
            viewModelScope.launch {
                insertGoal(deletedGoal)
                _state.update {
                    it.copy(
                        goals = state.value.goals + deletedGoal
                    )
                }
            }
        }
    }

    private fun handleGoalsResponse(response: Resource<List<Goal>>) {
        when (response) {
            is Resource.Success -> {
                val goals = response.data ?: emptyList()

                _state.update {
                    it.copy(
                        goals = if (isDoneGoalHidden) filterGoals(goals) else goals,
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

    private fun showGoalOverdueDialogIfNeeded(goals: List<Goal>) {
        if (showGoalOverdueDialog.not()) {
            return
        }

        goals.filter { goal ->
            isGoalOverdue(goal)
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