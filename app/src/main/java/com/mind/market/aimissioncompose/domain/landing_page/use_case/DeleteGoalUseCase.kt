package com.mind.market.aimissioncompose.domain.landing_page.use_case

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal

class DeleteGoalUseCase(
    private val repository: IGoalRepository
) {
    suspend operator fun invoke(goal: Goal) {
        repository.deleteGoal(goal, mode = GoalReadWriteOperation.FIREBASE_DATABASE)
    }
}