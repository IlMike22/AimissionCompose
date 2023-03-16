package com.mind.market.aimissioncompose.domain.goal

import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import java.time.LocalDate
import java.time.LocalDateTime

class IsGoalOverdueUseCase() {
    operator fun invoke(goal: Goal) =
        goal.finishDate < LocalDateTime.now() && goal.status != Status.DONE

}