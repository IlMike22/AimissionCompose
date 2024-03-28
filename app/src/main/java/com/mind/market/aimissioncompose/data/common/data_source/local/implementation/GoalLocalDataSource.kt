package com.mind.market.aimissioncompose.data.common.data_source.local.implementation

import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.common.data_source.IGoalDataSource
import com.mind.market.aimissioncompose.data.toGoal
import com.mind.market.aimissioncompose.data.toGoalDto
import com.mind.market.aimissioncompose.data.toStatusData
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status

class GoalLocalDataSource(
    private val goalDao: IGoalDao,
) : IGoalDataSource {
    override suspend fun deleteGoal(
        goal: Goal,
        userId: String?
    ):Boolean {
        goalDao.deleteGoal(goal.toGoalDto())
        return true
    }

    override suspend fun getGoals(userId: String?, onResult: (Throwable?, List<Goal>) -> Unit) {
        try {
            val goals = goalDao.getGoals().map { it.toGoal() }
            onResult(null, goals)
        } catch (exception: java.lang.Exception) {
            onResult(exception, emptyList())
        }
    }

    override suspend fun insertGoal(goal: Goal, userId: String?):Throwable? {
        goalDao.insert(goal.toGoalDto())
        return null
    }

    override suspend fun update(goal: Goal, userId: String?):Boolean {
        goalDao.update(goal.toGoalDto())
        return true
    }

    override suspend fun deleteAll(userId: String?, onResult: (Boolean) -> Unit) {
        try {
            goalDao.deleteAll()
            onResult(true)
        } catch (exc: Exception) {
            onResult(false)
        }
    }

    override suspend fun updateStatus(id: Int, newStatus: Status, userId: String?) {
        goalDao.updateStatus(id, newStatus.toStatusData())
    }

    override suspend fun getGoal(id: Int, userId: String?, onResult: (Throwable?, Goal?) -> Unit) {
        val goal = goalDao.getGoal(id)
        onResult(null, goal.toGoal())
    }
}