package com.mind.market.aimissioncompose.presentation.landing_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.auth.domain.LogoutUserUseCase
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.goal.DeleteGoalUseCase
import com.mind.market.aimissioncompose.domain.goal.GetGoalsUseCase
import com.mind.market.aimissioncompose.domain.goal.InsertGoalUseCase
import com.mind.market.aimissioncompose.domain.goal.IsGoalOverdueUseCase
import com.mind.market.aimissioncompose.domain.goal.UpdateGoalStatusUseCase
import com.mind.market.aimissioncompose.domain.landing_page.use_case.SetSortingModeUseCase
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.GoalListItem
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.domain.settings.use_case.GetUserSettingsUseCase
import com.mind.market.aimissioncompose.presentation.common.DropDownItem
import com.mind.market.aimissioncompose.presentation.common.DropDownItemId
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import com.mind.market.aimissioncompose.presentation.utils.SortingMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
    private val getUserSettings: GetUserSettingsUseCase,
    private val setSortingMode: SetSortingModeUseCase
) : ViewModel(), ICommandReceiver {
    private val _uiState = MutableStateFlow(LandingPageUiState())
    private val _goals = MutableStateFlow(emptyList<Goal>())
    private val _searchText = MutableStateFlow("")
    @OptIn(FlowPreview::class)
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
            goalItems = if (searchText.isNotBlank()) searchResult.createGoalListItems() else goals.createGoalListItems(),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        LandingPageUiState()
    )

    private var isDoneGoalHidden = false
    private var showGoalOverdueDialog = false
    private lateinit var deletedGoal: Goal
    private var goalIndex = -1

    private val _uiEvent = Channel<LandingPageUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        //TODO MIC this does not work atm (with zip) but would be a great refactoring
//        viewModelScope.launch(context = Dispatchers.IO) {
//            getGoals().zip(getUserSettings()) { goals, settings ->
//                handleGoalsResponse(goals)
//                isDoneGoalHidden = settings.isHideDoneGoals
//                showGoalOverdueDialog = settings.showGoalOverdueDialog
//                if (settings.selectedSortingMode != null) {
//                    _uiState.update { it.copy(selectedSortingMode = settings.selectedSortingMode) }
//                }
//            }
//        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getGoals().collect {
                handleGoalsResponse(it)
            }
        }
        viewModelScope.launch {
            getUserSettings().collect { userSettings ->
                isDoneGoalHidden = userSettings.isHideDoneGoals
                showGoalOverdueDialog = userSettings.showGoalOverdueDialog
                if (userSettings.selectedSortingMode != null) {
                    _uiState.update { it.copy(selectedSortingMode = userSettings.selectedSortingMode) }
                }
            }
        }
    }

    override fun onAddGoalClicked() {
        navigateToAddGoalScreen()
    }

    override fun onNavigateToDetailGoal(goal: Goal?) {
        onGoalContainerClicked(goal)
    }

    override fun onNavigateToAuthenticationScreen() {
        TODO("Not yet implemented")
    }

    override fun onDeleteGoalClicked(goal: Goal) {
        cacheAndDeleteGoal(goal)
    }

    override fun onStatusChangedClicked(goal: Goal) {
        goal.apply {
            viewModelScope.launch {
                val oldGoals = _goals.value
                val oldGoal = oldGoals.firstOrNull {
                    it.id == this@apply.id
                }
                val newGoals = mutableListOf<Goal>()
                var newGoal: Goal?
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
        }
    }

    override fun onUndoDeletedGoalClicked() {
        restoreDeletedGoal()
    }

    override fun onShowDialog(title: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun onLogoutUserClicked() {
        viewModelScope.launch {
            logoutUser { navigateToAuthenticationScreen() }
        }
    }

    override fun onGoalUpdated() {
        viewModelScope.launch {
            getGoals().collect { handleGoalsResponse(it) }
        }
    }

    override fun onSearchTextUpdate(newText: String) {
        _searchText.value = newText
    }

    override fun onDropDownStateChanged(isVisible: Boolean) {
        _uiState.update { it.copy(isDropDownExpanded = isVisible) }
    }

    override fun onSortingChanged(sortMode: SortingMode) {
        _uiState.update { it.copy(isDropDownExpanded = false) }
        viewModelScope.launch { setSortingMode(sortMode) }
        when (sortMode) {
            SortingMode.SORT_BY_GOALS_IN_PROGRESS -> {
                _goals.update { goals ->
                    goals.sortedByDescending { it.status == Status.IN_PROGRESS }
                }
                _uiState.update { it.copy(selectedSortingMode = SortingMode.SORT_BY_GOALS_IN_PROGRESS) }
            }

            SortingMode.SORT_BY_GOALS_IN_TODO -> {
                _goals.update { goals ->
                    goals.sortedByDescending { it.status == Status.TODO }
                }
                _uiState.update { it.copy(selectedSortingMode = SortingMode.SORT_BY_GOALS_IN_TODO) }
            }

            SortingMode.SORT_BY_GOALS_COMPLETED -> {
                _goals.update { goals ->
                    goals.sortedByDescending { it.status == Status.DONE }
                }
                _uiState.update { it.copy(selectedSortingMode = SortingMode.SORT_BY_GOALS_COMPLETED) }
            }

            SortingMode.SORT_BY_GOALS_DEPRECATED -> {
                _goals.update { goals ->
                    goals.sortedByDescending { it.status == Status.DEPRECATED }
                }
                _uiState.update { it.copy(selectedSortingMode = SortingMode.SORT_BY_GOALS_DEPRECATED) }
            }
        }
    }

    override fun onClearSearchText() {
        _searchText.value = ""
    }

    override fun onDropDownItemClicked(item: DropDownItem) {
        when (item.id) {
            DropDownItemId.DELETE_GOAL -> {
                cacheAndDeleteGoal(item.correspondingGoal)
            }

            DropDownItemId.HIDE_GOAL -> {
                _uiState.update {
                    it.copy(errorMessage = "This feature is not implemented yet. Please come back later.")
                }
            }
        }
    }

    override fun onResetErrorStateCommand() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    override fun onNavigateToStocksDiaryOverview() {
        viewModelScope.launch {
            _uiEvent.send(LandingPageUiEvent.NavigateToStocksDiaryOverviewScreen)
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

    private fun cacheAndDeleteGoal(goal: Goal?) {
        if (goal == null) {
            return
        }
        deletedGoal = goal
        goalIndex = _goals.value.indexOf(goal)
        goal.apply {
            viewModelScope.launch {
                try {
                    val isGoalDeleted = deleteGoal(goal)
                    if (isGoalDeleted.not()) {
                        _uiState.update {
                            it.copy(errorMessage = "Goal could not be deleted. Try again.")
                        }
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

                } catch (exception: java.lang.Exception) {
                    _uiEvent.send(LandingPageUiEvent.ShowSnackbar(message = "The goal could not be deleted. Try again."))
                }
            }
        }
    }

    private fun restoreDeletedGoal() {
        if (deletedGoal != Goal.EMPTY) {
            viewModelScope.launch {
                val error = insertGoal(deletedGoal)
                if (error == null) {
                    _goals.update { _goals.value.plus(deletedGoal) }
                } else {
                    _uiState.update { it.copy(errorMessage = error.message) }
                }
            }
        }
    }

    private fun onInsertGoalError(error: Throwable?) {
        _uiState.update { it.copy(errorMessage = error?.message) }
    }

    private fun handleGoalsResponse(response: Resource<List<Goal>>) {
        when (response) {
            is Resource.Success -> {
                viewModelScope.launch {
                    _goals.value = filterGoals(
                        goals = response.data ?: emptyList()
                    ).let {
                        checkForDeprecatedGoals(it)
                    }
                    _uiState.update {
                        it.copy(
                            hasResults = response.data?.isNotEmpty() ?: false,
                            isLoading = false,
                            errorMessage = null,
                            requestSearchTextFocus = true,
                            dropDownItems = listOf(
                                DropDownItem(
                                    id = DropDownItemId.DELETE_GOAL,
                                    name = "Delete goal"
                                ),
                                DropDownItem(
                                    id = DropDownItemId.HIDE_GOAL,
                                    name = "Hide goal"
                                )
                            )
                        )
                    }
                    val currentSortingMode = _uiState.value.selectedSortingMode
                    if (currentSortingMode != null) {
                        onSortingChanged(currentSortingMode)
                    }
                    showGoalOverdueDialogIfNeeded(_goals.value)
                }
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
                    year = it.key.year,
                    month = it.key.month,
                    monthValue = it.key.month.toMonthName(),
                    yearValue = it.key.year.toString(),
                    goals = it.value
                )
            )
        }
        return goalListItems.sortedWith(compareByDescending<GoalListItem> { it.year }.thenByDescending { it.month })
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

    private suspend fun checkForDeprecatedGoals(goals: List<Goal>): List<Goal> {
        val result = goals.map { goal ->
            if (goal.status != Status.DONE && goal.finishDate <= LocalDateTime.now().minusDays(1)) {
                updateGoalStatus(goal.id, Status.DEPRECATED) {}
                goal.copy(status = Status.DEPRECATED)
            } else {
                goal
            }
        }
        return result
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