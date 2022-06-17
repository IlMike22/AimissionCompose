package com.mind.market.aimissioncompose.presentation.landing_page

import com.example.aimissionlite.models.domain.Goal

sealed class LandingPageUiEvent(val message: String? = null, val goal: Goal? = null) {
    class ShowSnackbar(message: String) : LandingPageUiEvent(message)
    class NavigateToDetail(goal: Goal? = null) : LandingPageUiEvent(goal = goal)
    class NavigateToInfo : LandingPageUiEvent()
    class NavigateToSettings : LandingPageUiEvent()
}
