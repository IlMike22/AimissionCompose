package com.mind.market.aimissioncompose.data.common.repository

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.flow.Flow

interface IGoalRepository {
    suspend fun insert(goal: Goal, mode: GoalReadWriteOperation = GoalReadWriteOperation.LOCAL_DATABASE)

    suspend fun getGoal(id: Int, operation: GoalReadWriteOperation): Flow<Resource<Goal>>

    suspend fun deleteAll(): Boolean

    suspend fun updateStatus(id: Int, status: Status)

    suspend fun deleteGoal(goal: Goal, mode: GoalReadWriteOperation)

    suspend fun updateGoal(goal: Goal): Boolean

    fun getGoals(operation: GoalReadWriteOperation): Flow<Resource<List<Goal>>>
}