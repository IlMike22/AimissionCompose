package com.mind.market.aimissioncompose.presentation.landing_page

import com.example.aimissionlite.models.domain.Goal

data class LandingPageState(
    val goals: List<Goal> = emptyList()
)
