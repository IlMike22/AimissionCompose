package com.mind.market.aimissioncompose.domain.landing_page.use_case.implementation

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.domain.landing_page.use_case.GoalOperation
import com.mind.market.aimissioncompose.domain.landing_page.use_case.ILandingPageUseCase
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class LandingPageUseCase(
    private val statisticsRepository: IStatisticsRepository,
    private val goalRepository: IGoalRepository
) : ILandingPageUseCase {
    override fun isGoalOverdue(goal: Goal): Boolean =
        goal.finishDate < LocalDate.now() && goal.status != Status.DONE

    override suspend fun doesStatisticExist(month: Int, year: Int) =
        statisticsRepository.getStatisticsEntityByDate(month, year) == StatisticsEntity.EMPTY

    override suspend fun insertStatisticEntity(statistic: StatisticsEntity) {
        statisticsRepository.insertStatisticsEntity(entity = statistic)
    }

    override suspend fun executeGoalOperation(operation: GoalOperation) {
        when (operation) {
            is GoalOperation.Delete -> goalRepository.deleteGoal(operation.goalEntity)
            GoalOperation.DeleteAll -> goalRepository.deleteAll()
            is GoalOperation.Get -> goalRepository.getGoal(operation.id)
            is GoalOperation.Insert -> goalRepository.insert(operation.goalEntity)
            is GoalOperation.UpdateStatus -> goalRepository.updateStatus(
                operation.id,
                operation.newStatus
            )
        }
    }

    override fun getGoals(): Flow<Resource<List<Goal>>> = goalRepository.getGoals()
}