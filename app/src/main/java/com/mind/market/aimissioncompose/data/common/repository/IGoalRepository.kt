package com.mind.market.aimissioncompose.data.common.repository

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.flow.Flow

interface IGoalRepository {
    suspend fun insert(
        goal: Goal,
        mode: GoalReadWriteOperation = GoalReadWriteOperation.LOCAL_DATABASE
    ):Throwable?

    suspend fun getGoal(id: Int, operation: GoalReadWriteOperation): Flow<Resource<Goal>>

    suspend fun deleteAll(
        mode: GoalReadWriteOperation = GoalReadWriteOperation.FIREBASE_DATABASE,
        onResult: (Boolean) -> Unit
    )

    suspend fun updateStatus(
        id: Int,
        status: Status,
        operation: GoalReadWriteOperation = GoalReadWriteOperation.FIREBASE_DATABASE,
        onResult: (Status) -> Unit
    )

    suspend fun deleteGoal(
        goal: Goal,
        mode: GoalReadWriteOperation
    ):Boolean

    suspend fun updateGoal(goal: Goal, operation: GoalReadWriteOperation): Boolean

    fun getGoals(operation: GoalReadWriteOperation): Flow<Resource<List<Goal>>>
}