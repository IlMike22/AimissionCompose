package com.mind.market.aimissioncompose.data.goal.repository

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.data.goal.FAKE_GOAL
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGoalRepository : IGoalRepository {
    override suspend fun insert(
        goal: Goal,
        onResult: (Throwable?) -> Unit,
        mode: GoalReadWriteOperation
    ) {
        if (goal == Goal.EMPTY) {
            onResult(Throwable("Goal is not set. Cannot add an empty goal."))
            return
        }
        onResult(null)
    }

    override suspend fun getGoal(id: Int, operation: GoalReadWriteOperation): Flow<Resource<Goal>> {
        return flow {
            if (id > -1) {
                emit(Resource.Success(FAKE_GOAL))
            } else {
                emit(Resource.Error(null, message = "Id is invalid."))
            }
        }
    }

    override suspend fun deleteAll(mode: GoalReadWriteOperation, onResult: (Boolean) -> Unit) {
        onResult(true)
    }

    override suspend fun updateStatus(
        id: Int,
        status: Status,
        operation: GoalReadWriteOperation,
        onResult: (Status) -> Unit
    ) {
        onResult(status)
    }


    override suspend fun deleteGoal(
        goal: Goal,
        onResult: (Boolean) -> Unit,
        mode: GoalReadWriteOperation
    ) {
        if (goal == Goal.EMPTY) {
            onResult(false)
            return
        }
        onResult(true)
    }

    override suspend fun updateGoal(
        goal: Goal,
        operation: GoalReadWriteOperation,
        onResult: (Boolean) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getGoals(operation: GoalReadWriteOperation): Flow<Resource<List<Goal>>> {
        return flow {
            emit(Resource.Success(data = listOf(FAKE_GOAL, FAKE_GOAL)))
        }
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