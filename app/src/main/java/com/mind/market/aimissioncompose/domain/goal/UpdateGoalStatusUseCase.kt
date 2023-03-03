package com.mind.market.aimissioncompose.domain.goal

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.landing_page.use_case.GoalOperation
import com.mind.market.aimissioncompose.domain.models.Status

class UpdateGoalStatusUseCase(
    private val repository: IGoalRepository
) {
    suspend operator fun invoke(operation: GoalOperation, onResult: (Status) -> Unit) {
        when (operation) {
            is GoalOperation.UpdateStatus -> {
                val newStatus = updateGoalStatus(operation.oldStatus)
                repository.updateStatus(
                    id = operation.id,
                    status = newStatus,
                    operation = GoalReadWriteOperation.FIREBASE_DATABASE,
                    onResult = onResult
                )
            }
        }
    }

    private fun updateGoalStatus(oldStatus: Status): Status {
        return when (oldStatus) {
            Status.TODO -> Status.IN_PROGRESS
            Status.IN_PROGRESS -> Status.DONE
            Status.DONE -> Status.DEPRECATED
            Status.DEPRECATED -> Status.TODO
            Status.UNKNOWN -> Status.UNKNOWN
        }
    }
}