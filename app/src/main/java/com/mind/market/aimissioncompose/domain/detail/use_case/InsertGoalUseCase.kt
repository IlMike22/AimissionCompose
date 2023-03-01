package com.mind.market.aimissioncompose.domain.detail.use_case

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal

class InsertGoalUseCase(
    private val repository: IGoalRepository
) {
    suspend operator fun invoke(goal: Goal, operation: GoalReadWriteOperation) {
        repository.insert(goal, operation)
    }
}