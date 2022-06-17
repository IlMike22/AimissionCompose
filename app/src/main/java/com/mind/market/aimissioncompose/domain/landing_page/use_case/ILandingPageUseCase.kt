package com.example.aimissionlite.domain.landing_page.use_case

import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import kotlinx.coroutines.flow.Flow

interface ILandingPageUseCase {
    suspend fun deleteAllGoals(): Boolean

    fun getAllGoals(): Flow<List<Goal>>

    suspend fun updateGoalStatus(
        id: Int,
        status: Status
    )

    suspend fun deleteGoal(goal: Goal): Boolean

    suspend fun insertGoal(goal:Goal)

    fun getDeleteGoalsOnStartup(): Flow<Boolean>
}