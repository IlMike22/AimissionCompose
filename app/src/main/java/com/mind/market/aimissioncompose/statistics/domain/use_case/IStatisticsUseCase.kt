package com.mind.market.aimissioncompose.statistics.domain.use_case

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

interface IStatisticsUseCase {
    fun getStatistics():  Flow<Resource<Flow<List<StatisticsEntity>>>>
}