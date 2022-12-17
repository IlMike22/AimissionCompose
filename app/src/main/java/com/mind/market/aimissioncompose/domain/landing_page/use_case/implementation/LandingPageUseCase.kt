package com.mind.market.aimissioncompose.domain.landing_page.use_case.implementation

import com.mind.market.aimissioncompose.domain.landing_page.use_case.ILandingPageUseCase
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import java.time.LocalDate

class LandingPageUseCase() : ILandingPageUseCase {
    override fun isGoalOverdue(goal: Goal): Boolean =
        goal.finishDate < LocalDate.now() && goal.status != Status.DONE
}