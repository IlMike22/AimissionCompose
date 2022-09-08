package com.mind.market.aimissioncompose.presentation.detail

import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Priority

data class DetailState(
    val goal: Goal = Goal.EMPTY,
    val selectedPriority: Priority? = null
)
