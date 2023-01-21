package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import com.mind.market.aimissioncompose.statistics.domain.use_case.IStatisticsUseCase
import kotlinx.coroutines.flow.Flow

class StatisticsUseCase(
    private val repository: IStatisticsRepository
) : IStatisticsUseCase {
    override fun getStatistics(): Flow<Resource<Flow<List<StatisticsEntity>>>> {
        return repository.getStatisticsEntities()
    }
}