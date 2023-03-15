package com.mind.market.aimissioncompose.presentation.landing_page

import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction

sealed class LandingPageUiEvent(val goal: Goal? = null) {
    data class ShowSnackbar(
        val message: String,
        val snackbarAction: SnackBarAction? = null
    ) : LandingPageUiEvent()

    object OnAddGoalClicked : LandingPageUiEvent()
    class NavigateToDetailGoal(goal: Goal? = null) : LandingPageUiEvent(goal = goal)
    class OnDeleteGoalClicked(goal: Goal) : LandingPageUiEvent(goal = goal)
    class OnStatusChangedClicked(goal: Goal) : LandingPageUiEvent(goal = goal)
    object OnUndoDeleteGoalClicked : LandingPageUiEvent()
    data class ShowGoalOverdueDialog(val title: String, val message: String) : LandingPageUiEvent()
    object OnLogoutUserClicked : LandingPageUiEvent()
    object OnGoalUpdate : LandingPageUiEvent()
    data class OnSearchTextUpdate(val newText: String) : LandingPageUiEvent()
    class OnDropDownStateChanged(val isVisible: Boolean = false) : LandingPageUiEvent()
    class OnSortingChanged(val sortMode: SortingMode): LandingPageUiEvent()
}

enum class SortingMode {
    SORT_BY_GENRE,
    SORT_BY_STATUS,
    SORT_BY_GOALS_COMPLETED,
}
