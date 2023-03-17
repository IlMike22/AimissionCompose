package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimissionlite.domain.settings.use_case.ISettingsUseCase
import com.mind.market.aimissioncompose.auth.domain.LogoutUserUseCase
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.goal.*
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.GoalListItem
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import com.mind.market.aimissioncompose.presentation.utils.SortingMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    private val settingsUseCase: ISettingsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LandingPageUiState())
    private val _goals = MutableStateFlow(emptyList<Goal>())
    private val _searchText = MutableStateFlow("")
    private val _searchResult = _searchText
        .debounce(1000L)
        .onEach {
            _uiState.update { it.copy(isLoading = true, requestSearchTextFocus = true) }
        }
        .combine(_goals) { text, goals ->
            if (text.isBlank()) goals
            else {
                delay(1000L)
                goals.filter { goal ->
                    goal.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    requestSearchTextFocus = true
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            _goals.value
        )
    val state = combine(
        _uiState,
        _searchText,
        _goals,
        _searchResult
    ) { state, searchText, goals, searchResult ->
        state.copy(
            searchText = searchText,
            goalItems = if (searchText.isNotBlank()) searchResult.createGoalListItems() else goals.createGoalListItems()
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        LandingPageUiState()
    )

    var isDeleteAllGoals: LiveData<Boolean>? = null
    private var isDoneGoalHidden = false
    private var showGoalOverdueDialog = false
    private lateinit var deletedGoal: Goal
    private var goalIndex = -1

    private val _uiEvent = Channel<LandingPageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            getGoals().collect {
                handleGoalsResponse(it)
            }
            launch {
                settingsUseCase.getUserSettings().collect { userSettings ->
                    isDoneGoalHidden = userSettings.isHideDoneGoals
                    showGoalOverdueDialog = userSettings.showGoalOverdueDialog
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
                        val oldGoals = _goals.value
                        val oldGoal = oldGoals.firstOrNull {
                            it.id == this@apply.id
                        }
                        val newGoals = mutableListOf<Goal>()
                        var newGoal: Goal? = null
                        updateGoalStatus(id, status) { newStatus ->
                            oldGoals.forEach {
                                if (it.id == oldGoal?.id) {
                                    newGoal = it.copy(
                                        status = newStatus
                                    )
                                    newGoals.add(newGoal as Goal)
                                } else {
                                    newGoals.add(it)
                                }
                            }
                            _goals.value = newGoals
                        }
                    }
                } ?: _uiState.update {
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
                    logoutUser { navigateToAuthenticationScreen() }
                }
            }
            is LandingPageUiEvent.ShowGoalOverdueDialog -> TODO()
            is LandingPageUiEvent.ShowSnackbar -> TODO()
            is LandingPageUiEvent.OnSearchTextUpdate -> {
                _searchText.value = event.newText
            }
            is LandingPageUiEvent.OnDropDownStateChanged -> {
                _uiState.update { it.copy(isDropDownExpanded = event.isVisible) }
            }
            is LandingPageUiEvent.OnSortingChanged -> {
                _uiState.update { it.copy(isDropDownExpanded = false) }
                when (event.sortMode) {
                    SortingMode.SORT_BY_GOALS_IN_PROGRESS -> {
                        _goals.update { goals ->
                            goals.sortedByDescending { it.status == Status.IN_PROGRESS }
                        }
                        _uiState.update { it.copy(selectedSortMode = SortingMode.SORT_BY_GOALS_IN_PROGRESS) }
                    }
                    SortingMode.SORT_BY_GOALS_IN_TODO -> {
                        _goals.update { goals ->
                            goals.sortedByDescending { it.status == Status.TODO }
                        }
                        _uiState.update { it.copy(selectedSortMode = SortingMode.SORT_BY_GOALS_IN_TODO) }
                    }
                    SortingMode.SORT_BY_GOALS_COMPLETED -> {
                        _goals.update { goals ->
                            goals.sortedByDescending { it.status == Status.DONE }
                        }
                        _uiState.update { it.copy(selectedSortMode = SortingMode.SORT_BY_GOALS_COMPLETED) }
                    }
                    SortingMode.SORT_BY_GOALS_DEPRECATED -> {
                        _goals.update { goals ->
                            goals.sortedByDescending { it.status == Status.DEPRECATED }
                        }
                        _uiState.update { it.copy(selectedSortMode = SortingMode.SORT_BY_GOALS_DEPRECATED) }
                    }
                }
            }
            LandingPageUiEvent.OnClearSearchText -> {
                _searchText.value = ""
            }
        }
    }

    private fun navigateToAddGoalScreen() {
        viewModelScope.launch {
            _uiEvent.send(LandingPageUiEvent.NavigateToDetailGoal())
        }
    }

    private fun navigateToAuthenticationScreen() {
        viewModelScope.launch {
            _uiEvent.send(LandingPageUiEvent.NavigateToAuthenticationScreen)
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
        goalIndex = _goals.value.indexOf(goal)
        goal.apply {
            viewModelScope.launch {
                try {
                    deleteGoal(goal) { isGoalDeleted ->
                        if (isGoalDeleted.not()) {
                            _uiState.update {
                                it.copy(errorMessage = "Goal could not be deleted. Try again.")
                            }
                            return@deleteGoal
                        }

                        deletedGoal = goal
                        val updatedGoals = mutableListOf<Goal>()
                        _goals.value.forEach {
                            if (it != deletedGoal) {
                                updatedGoals.add(it)
                            }
                        }
                        _goals.value = updatedGoals
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
                _goals.value + deletedGoal
            }
        }
    }

    private fun handleGoalsResponse(response: Resource<List<Goal>>) {
        when (response) {
            is Resource.Success -> {
                _goals.value = filterGoals(response.data ?: emptyList())
                _uiState.update {
                    it.copy(
                        hasResults = response.data?.isNotEmpty() ?: false,
                        isLoading = false,
                        errorMessage = null,
                        requestSearchTextFocus = true
                    )
                }

                showGoalOverdueDialogIfNeeded(_goals.value)
            }

            is Resource.Loading -> {
                _uiState.update {
                    it.copy(
                        isLoading = response.isLoading,
                        requestSearchTextFocus = false
                    )
                }
            }
            is Resource.Error -> {
                _uiState.update {
                    _goals.value = emptyList()
                    it.copy(
                        isLoading = false,
                        hasResults = false,
                        requestSearchTextFocus = false,
                        errorMessage = response.message ?: "Unknown error while loading data."
                    )
                }
            }
        }
    }

    private fun List<Goal>.createGoalListItems(): List<GoalListItem> {
        val monthYearValuesFound = mutableMapOf<MonthYearValue, String>()
        val goalsPerMonthAndYear = mutableMapOf<MonthYearValue, MutableList<Goal>>()
        val goalListItems = mutableListOf<GoalListItem>()
        this.forEach { goal ->
            monthYearValuesFound[MonthYearValue(goal.creationDate.month, goal.creationDate.year)] =
                goal.creationDate.month.toMonthName()
        }

        this.forEach { goal ->
            monthYearValuesFound.keys.find {
                goal.creationDate.month == it.month && goal.creationDate.year == it.year
            }.apply {
                if (this != null && goalsPerMonthAndYear[this] == null) {
                    goalsPerMonthAndYear[this] = mutableListOf()
                }
                goalsPerMonthAndYear[this]?.add(goal)
            }
        }

        goalsPerMonthAndYear.forEach {
            goalListItems.add(
                GoalListItem(
                    monthValue = it.key.month.toMonthName(),
                    yearValue = it.key.year.toString(),
                    goals = it.value
                )
            )
        }
        return goalListItems
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

    private fun Goal.doesMatchSearchQuery(query: String): Boolean {
        return try {
            val matchingCombinations = listOf(
                title,
                description,
                "${title.first()}${title[1]}",
                "${title.first()}${title[1]}${title[2]}",
                "${description.first()}",
                "${description.first()}${description[1]}",
                "${description.first()}${description[1]}${description[2]}",
            )

            matchingCombinations.any {
                it.contains(query, ignoreCase = true)
            }
        } catch (exception: MatchSearchQueryException) {
            false
        }
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

    data class MonthYearValue(
        val month: Month,
        val year: Int
    )
}