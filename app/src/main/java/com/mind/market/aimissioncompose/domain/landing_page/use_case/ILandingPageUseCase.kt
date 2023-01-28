package com.mind.market.aimissioncompose.domain.landing_page.use_case

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

interface ILandingPageUseCase { // TODO MIC extract every single method in an own UseCase
    fun isGoalOverdue(goal: Goal): Boolean
    suspend fun doesStatisticExist(month: Int, year: Int): Boolean

    suspend fun insertStatisticEntity(statistic: StatisticsEntity)
    suspend fun executeGoalOperation(operation: GoalOperation)
    fun getGoals(): Flow<Resource<List<Goal>>>
}

sealed class GoalOperation(val goal: Goal? = null) {
    data class UpdateStatus(val id: Int, val newStatus: Status) : GoalOperation()
    data class Delete(val goalEntity: Goal) : GoalOperation(goalEntity)
    object DeleteAll : GoalOperation()
    data class Insert(val goalEntity: Goal) : GoalOperation()
    data class Get(val id: Int, val goalEntity:Goal? = null) : GoalOperation(goalEntity)
}