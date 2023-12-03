package com.mind.market.aimissioncompose.presentation.landing_page

import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.presentation.common.DropDownItem
import com.mind.market.aimissioncompose.presentation.common.SnackBarAction
import com.mind.market.aimissioncompose.presentation.utils.SortingMode

sealed class LandingPageUiEvent(val goal: Goal? = null) {
    data class ShowSnackbar(
        val message: String,
        val snackbarAction: SnackBarAction? = null
    ) : LandingPageUiEvent()
    class NavigateToDetailGoal(goal: Goal? = null) : LandingPageUiEvent(goal = goal)
    object NavigateToAuthenticationScreen : LandingPageUiEvent()
    object NavigateToStocksDiaryOverviewScreen: LandingPageUiEvent()
    data class ShowGoalOverdueDialog(val title: String, val message: String) : LandingPageUiEvent()

}
