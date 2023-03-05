package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository

class InsertStatisticEntityUseCase(
    private val repository: IStatisticsRepository
) {
    suspend operator fun invoke(entity: StatisticsEntity) {
        repository.insertStatisticsEntity(entity)
    }
}