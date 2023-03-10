package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository

class InsertStatisticUseCase(
    private val repository: IStatisticsRepository
) {
    suspend operator fun invoke(entity: StatisticsEntity, onResult: (Boolean) -> Unit) {
        repository.insertEntity(
            entity = entity,
            onResult = onResult,
            operation = GoalReadWriteOperation.FIREBASE_DATABASE
        )
    }
}