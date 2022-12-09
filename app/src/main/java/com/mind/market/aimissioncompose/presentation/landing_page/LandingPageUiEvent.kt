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
    object OnListUpdated : LandingPageUiEvent()
}
