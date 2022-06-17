package com.example.aimissionlite.domain.landing_page.use_case.implementation

import com.example.aimissionlite.domain.landing_page.use_case.ILandingPageUseCase
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.data.settings.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow

class LandingPageUseCase(
    private val goalRepository: IGoalRepository,
    private val settingsRepository: ISettingsRepository
) : ILandingPageUseCase {
    override suspend fun deleteAllGoals(): Boolean = goalRepository.deleteAll()
    override fun getAllGoals(): Flow<List<Goal>> = goalRepository.getGoals()
    override suspend fun updateGoalStatus(id: Int, status: Status) =
        goalRepository.updateStatus(id, status)

    override suspend fun deleteGoal(goal: Goal): Boolean = goalRepository.deleteGoal(goal)
    override suspend fun insertGoal(goal: Goal) = goalRepository.insert(goal)
    override fun getDeleteGoalsOnStartup(): Flow<Boolean> =
        settingsRepository.getDeleteGoalsOnStartup()
}