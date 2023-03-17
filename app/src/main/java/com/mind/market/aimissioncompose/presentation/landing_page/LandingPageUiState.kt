package com.mind.market.aimissioncompose.presentation.landing_page

import com.mind.market.aimissioncompose.domain.models.GoalListItem
import com.mind.market.aimissioncompose.presentation.utils.SortingMode

data class LandingPageUiState(
    val goalItems: List<GoalListItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchText: String = "",
    val requestSearchTextFocus: Boolean = false,
    val hasResults: Boolean = false,
    val isDropDownExpanded: Boolean = false,
    val selectedSortMode: SortingMode? = null
)
