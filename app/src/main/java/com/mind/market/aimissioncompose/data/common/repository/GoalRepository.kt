package com.mind.market.aimissioncompose.data.common.repository

import androidx.annotation.CheckResult
import androidx.annotation.WorkerThread
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.toGoal
import com.mind.market.aimissioncompose.data.toGoalDto
import com.mind.market.aimissioncompose.data.toStatusData
import com.mind.market.aimissioncompose.domain.models.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

//TODO add GoalLocalDataSource because it`s not clear to go directly to Dao in Repo

class GoalRepository(private val goalDao: IGoalDao) : IGoalRepository {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(goal: Goal) {
        /**
        First we map the domain model to data before we use the dao to write the new goal into db.
         */
        goalDao.insert(goal.toGoalDto())
    }

    @WorkerThread
    override suspend fun getGoal(id: Int): Goal = goalDao.getGoal(id).toGoal()

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
    override suspend fun deleteGoal(goal: Goal)  {
        goalDao.deleteGoal(goal.toGoalDto())

    }

    @WorkerThread
    @CheckResult
    override suspend fun updateGoal(goal: Goal): Boolean {
        return try {
            goalDao.update(goal.toGoalDto())
            true
        } catch (exception: Exception) {
            false
        }
    }

    override fun getGoals(): Flow<Resource<List<Goal>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val goals = goalDao.getGoals()
                emit(Resource.Success(
                    goals.let { goalsDto ->
                        goalsDto.map { goalDto ->
                            goalDto.toGoal()
                        }
                    }
                ))
            } catch (exception: IOException) {
                exception.printStackTrace()
                emit(Resource.Error(message = "Could not load data."))
            }
            emit(Resource.Loading(false))
        }
    }
}