package com.mind.market.aimissioncompose.presentation.landing_page

import com.mind.market.aimissioncompose.domain.models.Goal

sealed class LandingPageUiEvent(val message: String? = null, val goal: Goal? = null) {
    class ShowSnackbar(message: String) : LandingPageUiEvent(message)
    object OnAddGoalClicked : LandingPageUiEvent()
    class NavigateToDetailGoal(goal: Goal? = null) : LandingPageUiEvent(goal = goal)
    class OnDeleteGoalClicked(goal: Goal) : LandingPageUiEvent(goal = goal)
//    class OnStatusChangedClicked(goal: Goal) : LandingPageUiEvent(goal = goal)
}
