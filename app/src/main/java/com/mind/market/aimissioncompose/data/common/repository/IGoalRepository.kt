package com.mind.market.aimissioncompose.data.common.repository

import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import kotlinx.coroutines.flow.Flow

interface IGoalRepository {
    suspend fun insert(goal: Goal)

    suspend fun getGoal(id: Int): Goal

    suspend fun deleteAll(): Boolean

    suspend fun updateStatus(id: Int, status: Status)

    suspend fun deleteGoal(goal: Goal): Boolean

    suspend fun updateGoal(goal: Goal): Boolean

    fun getGoals(): Flow<List<Goal>>
}