package com.mind.market.aimissioncompose.data.common.repository

import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.models.Goal
import kotlinx.coroutines.flow.Flow

interface IGoalRepository {
    suspend fun insert(goal: Goal)

    suspend fun getGoal(id: Int): Goal

    suspend fun deleteAll(): Boolean

    suspend fun updateStatus(id: Int, status: Status)

    suspend fun deleteGoal(goal: Goal)

    suspend fun updateGoal(goal: Goal): Boolean

    suspend fun getGoals(): Flow<Resource<List<Goal>>>
}