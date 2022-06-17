package com.mind.market.aimissioncompose.data.common.repository

import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.toStatusData
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import kotlinx.coroutines.flow.Flow

class GoalRepository(private val goalDao: IGoalDao) : IGoalRepository {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(goal: Goal) {
        /**
        First we map the domain model to data before we use the dao to write the new goal into db.
         */
        goalDao.insert(goal)
    }

    @WorkerThread
    override suspend fun getGoal(id: Int): Goal = goalDao.getGoal(id)

    @WorkerThread
    @CheckResult
    override suspend fun deleteAll(): Boolean {
        return try {
            val result = goalDao.deleteAll()
            println("!! Success on deleting all goals. Details: $result")
            true
        } catch (error: Throwable) {
            println("!! Error while deleting all goals. Details: ${error.message}")
            false
        }
    }

    @WorkerThread
    override suspend fun updateStatus(id: Int, status: Status) {
        goalDao.updateStatus(
            id = id,
            status = status.toStatusData()
        )
    }

    @WorkerThread
    @CheckResult
    override suspend fun deleteGoal(goal: Goal): Boolean {
        return try {
            goalDao.deleteGoal(goal)
            true
        } catch (exception: Exception) {
            false
        }
    }

    @WorkerThread
    @CheckResult
    override suspend fun updateGoal(goal: Goal): Boolean {
        return try {
            goalDao.update(goal)
            true
        } catch (exception: Exception) {
            false
        }
    }

    override fun getGoals(): Flow<List<Goal>> {
        return goalDao.getGoals()
    }
}