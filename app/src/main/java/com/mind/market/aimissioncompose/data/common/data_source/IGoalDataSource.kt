package com.mind.market.aimissioncompose.data.common.data_source

import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status

interface IGoalDataSource {
    suspend fun deleteGoal(goal: Goal, userId: String? = null): Boolean
    suspend fun getGoals(userId: String? = null, onResult: (Throwable?, List<Goal>) -> Unit)
    suspend fun insertGoal(goal: Goal, userId: String? = null): Throwable?
    suspend fun getGoal(id: Int, userId: String? = null, onResult: (Throwable?, Goal?) -> Unit)
    suspend fun update(goal: Goal, userId: String? = null): Boolean
    suspend fun deleteAll(userId: String? = null, onResult: (Boolean) -> Unit)
    suspend fun updateStatus(id: Int, newStatus: Status, userId: String? = null)
}