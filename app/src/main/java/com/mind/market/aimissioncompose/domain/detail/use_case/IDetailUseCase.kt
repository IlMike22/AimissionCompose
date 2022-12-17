package com.mind.market.aimissioncompose.domain.detail.use_case

import com.mind.market.aimissioncompose.domain.models.Goal

interface IDetailUseCase {
    suspend fun getGoal(id: Int): Goal
    suspend fun updateGoal(goal: Goal): Boolean
    suspend fun insert(goal: Goal)

    fun isGoalOverdue(goal: Goal): Boolean

}