package com.mind.market.aimissioncompose.presentation.landing_page

import com.mind.market.aimissioncompose.domain.models.Goal

data class LandingPageUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchText: String = "",
    val requestSearchTextFocus:Boolean = false,
    val hasResults:Boolean = false
)
