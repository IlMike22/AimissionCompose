package com.mind.market.aimissioncompose.presentation.landing_page

import com.mind.market.aimissioncompose.domain.models.Goal

sealed class LandingPageUiEvent(val message: String? = null, val goal: Goal? = null) {
    class ShowSnackbar(message: String) : LandingPageUiEvent(message)
    object OnAddGoalClicked : LandingPageUiEvent()
    class NavigateToDetailGoal(goal: Goal? = null) : LandingPageUiEvent(goal = goal)
    object NavigateToInfo : LandingPageUiEvent()
    object NavigateToSettings : LandingPageUiEvent()
    class OnDeleteGoalClicked(goal: Goal):LandingPageUiEvent()
    class OnStatusChangedClicked(goal: Goal):LandingPageUiEvent()
}
