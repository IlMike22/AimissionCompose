package com.mind.market.aimissioncompose.presentation.landing_page

import com.mind.market.aimissioncompose.domain.models.Goal

data class LandingPageState(
    val goals: List<Goal> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
