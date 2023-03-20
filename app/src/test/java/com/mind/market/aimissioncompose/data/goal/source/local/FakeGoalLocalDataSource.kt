package com.mind.market.aimissioncompose.data.goal.source.local

import com.mind.market.aimissioncompose.data.common.data_source.IGoalDataSource
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status

class FakeGoalLocalDataSource: IGoalDataSource {
    override suspend fun deleteGoal(goal: Goal, userId: String?, onResult: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getGoals(userId: String?, onResult: (Throwable?, List<Goal>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun insertGoal(goal: Goal, userId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun getGoal(id: Int, userId: String?, onResult: (Throwable?, Goal?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun update(goal: Goal, userId: String?, onResult: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(userId: String?, onResult: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun updateStatus(id: Int, newStatus: Status, userId: String?) {
        TODO("Not yet implemented")
    }
}