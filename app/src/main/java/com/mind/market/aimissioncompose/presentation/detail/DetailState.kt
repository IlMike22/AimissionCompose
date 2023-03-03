package com.mind.market.aimissioncompose.presentation.detail

import com.mind.market.aimissioncompose.domain.models.Goal

data class DetailState(
    val goal: Goal = Goal.EMPTY,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val ctaButtonText: String = ""
)
