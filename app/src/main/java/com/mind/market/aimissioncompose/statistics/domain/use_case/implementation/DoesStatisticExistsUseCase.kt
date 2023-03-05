package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository

class DoesStatisticExistsUseCase(
    private val repository: IStatisticsRepository
) {
    suspend operator fun invoke(month: Int, year: Int) =
        repository.getStatisticsEntityByDate(month, year) == StatisticsEntity.EMPTY

}