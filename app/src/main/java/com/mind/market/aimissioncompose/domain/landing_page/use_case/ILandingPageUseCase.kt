package com.mind.market.aimissioncompose.domain.landing_page.use_case

import com.mind.market.aimissioncompose.domain.models.Goal

interface ILandingPageUseCase {
    fun isGoalOverdue(goal: Goal): Boolean
}