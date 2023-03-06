package com.mind.market.aimissioncompose.domain.detail.use_case.implementation

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsOperation
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import java.time.LocalDateTime

class UpdateStatisticsWithNewGoalCreatedUseCase(
    private val statisticsRepository: IStatisticsRepository
) {
    suspend operator fun invoke(operation: StatisticsOperation, onResult: (Boolean) -> Unit = {}) {

        when (operation) {
            is StatisticsOperation.AddGoal -> {
                val currentDate = LocalDateTime.now()
                var newEntity: StatisticsEntity? = null

                val entity = statisticsRepository.getStatisticsEntityByDate(
                    currentDate.monthValue,
                    currentDate.year
                )

                if (entity == StatisticsEntity.EMPTY) {
                    statisticsRepository.insertStatisticsEntity(
                        entity,
                        onResult,
                        GoalReadWriteOperation.FIREBASE_DATABASE
                    )
                }

                statisticsRepository.updateStatisticEntity(
                    entity.copy(
                        amountGoalsCreated = entity.amountGoalsCreated + 1
                    ),
                    operation = GoalReadWriteOperation.LOCAL_DATABASE
                )
            }
        }
    }
}