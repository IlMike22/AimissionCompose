package com.mind.market.aimissioncompose.domain.goal

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.models.Status

class UpdateGoalStatusUseCase(
    private val repository: IGoalRepository
) {
    suspend operator fun invoke(goalId: Int, oldStatus: Status, onResult: (Status) -> Unit) {
        repository.updateStatus(
            id = goalId,
            status = if (oldStatus != Status.DEPRECATED) updateGoalStatus(oldStatus) else oldStatus,
            operation = GoalReadWriteOperation.FIREBASE_DATABASE,
            onResult = onResult
        )
    }

    private fun updateGoalStatus(oldStatus: Status): Status {
        return when (oldStatus) {
            Status.TODO -> Status.IN_PROGRESS
            Status.IN_PROGRESS -> Status.DONE
            Status.DONE -> Status.TODO
            else -> Status.UNKNOWN
        }
    }
}