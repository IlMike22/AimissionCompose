package com.mind.market.aimissioncompose.statistics.domain.use_case.implementation

import android.util.Log
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion

class GetStatisticsUseCase(
    private val repository: IStatisticsRepository
) {
    operator fun invoke(): Flow<Resource<List<StatisticsEntity>>> {
        return repository.getEntities()
    }
}