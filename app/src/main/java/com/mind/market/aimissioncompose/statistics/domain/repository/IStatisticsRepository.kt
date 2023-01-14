package com.mind.market.aimissioncompose.statistics.domain.repository

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

interface IStatisticsRepository {
    suspend fun getStatisticsEntity(id:Int): StatisticsEntity
    suspend fun updateStatisticEntity(entity:StatisticsEntity)
    fun getStatisticsEntities(): Flow<Resource<List<StatisticsEntity>>>
}