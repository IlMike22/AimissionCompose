package com.mind.market.aimissioncompose.data.common.data_source

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.domain.models.Goal

interface IGoalRemoteDataSource {
    suspend fun deleteGoal(goal: Goal, mode: GoalReadWriteOperation, userId: String)
    suspend fun insertGoal(goal: Goal, mode: GoalReadWriteOperation, userId: String)
    suspend fun getGoal(id: Int, userId: String, onResult: (Throwable?, Goal?) -> Unit)
}