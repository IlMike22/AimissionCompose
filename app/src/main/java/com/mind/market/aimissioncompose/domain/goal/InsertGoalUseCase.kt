package com.mind.market.aimissioncompose.domain.goal

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Goal

class InsertGoalUseCase(
    private val repository: IGoalRepository
) {
    suspend operator fun invoke(
        goal: Goal,
        onResult: (Throwable?) -> Unit,
        operation: GoalReadWriteOperation = GoalReadWriteOperation.FIREBASE_DATABASE
    ) {
        if (goal == Goal.EMPTY) {
            onResult(Throwable("Goal is not set. Cannot add an empty goal."))
            return
        }

        repository.insert(goal, onResult, operation)
    }
}