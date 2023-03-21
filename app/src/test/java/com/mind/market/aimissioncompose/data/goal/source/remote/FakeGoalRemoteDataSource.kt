package com.mind.market.aimissioncompose.data.goal.source.remote

import com.mind.market.aimissioncompose.data.common.data_source.IGoalDataSource
import com.mind.market.aimissioncompose.data.goal.FAKE_GOAL
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status

class FakeGoalRemoteDataSource: IGoalDataSource {
    override suspend fun deleteGoal(goal: Goal, userId: String?, onResult: (Boolean) -> Unit) {
        onResult(true)
    }

    override suspend fun getGoals(userId: String?, onResult: (Throwable?, List<Goal>) -> Unit) {
        if (userId == null || userId.isBlank()) {
            onResult(Throwable("UserId is blank"), emptyList())
            return
        }

        onResult(null, listOf(Goal.EMPTY, Goal.EMPTY))
    }

    override suspend fun insertGoal(goal: Goal, userId: String?, onResult: (Throwable?) -> Unit) {
        onResult(null)
    }

    override suspend fun getGoal(id: Int, userId: String?, onResult: (Throwable?, Goal?) -> Unit) {
        if (id > 0) {
            onResult(null, FAKE_GOAL)
        } else {
            onResult(Throwable(message = "Id is not set"), null)
        }
    }

    override suspend fun update(goal: Goal, userId: String?, onResult: (Boolean) -> Unit) {
        if (goal == Goal.EMPTY) {
            onResult(false)
            return
        }
        onResult(true)
    }

    override suspend fun deleteAll(userId: String?, onResult: (Boolean) -> Unit) {
        if (userId != null) {
            onResult(true)
            return
        }
        onResult(false)
    }

    override suspend fun updateStatus(id: Int, newStatus: Status, userId: String?) {}
}