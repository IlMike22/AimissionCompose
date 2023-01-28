package com.mind.market.aimissioncompose.domain.detail.use_case.implementation

import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import java.time.LocalDateTime

class UpdateStatisticsWithNewGoalCreatedUseCase(
    private val statisticsRepository: IStatisticsRepository
) {
    suspend operator fun invoke(newGoal: Goal) { // TODO MIC go on here next time
        val currentDate = LocalDateTime.now()
        var newEntity: StatisticsEntity? = null

        val entity = statisticsRepository.getStatisticsEntityByDate(
            currentDate.monthValue,
            currentDate.year
        )

        newEntity = entity.copy(
            amountGoalsCreated = entity.amountGoalsCreated + 1
        )
        statisticsRepository.updateStatisticEntity(newEntity)
    }
}