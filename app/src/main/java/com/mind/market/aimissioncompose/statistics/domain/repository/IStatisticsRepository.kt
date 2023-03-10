package com.mind.market.aimissioncompose.statistics.domain.repository

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

interface IStatisticsRepository {
    suspend fun getStatisticsEntity(id: String, onResult: (Throwable?, StatisticsEntity?) -> Unit)
    suspend fun getStatisticsEntityByDate(
        month: Int,
        year: Int,
        onResult: (Throwable?, StatisticsEntity?) -> Unit
    )

    suspend fun insertStatisticsEntity(
        entity: StatisticsEntity,
        onResult: (Boolean) -> Unit,
        operation: GoalReadWriteOperation
    )

    suspend fun updateStatisticEntity(entity: StatisticsEntity)
    fun getStatisticsEntities(): Flow<Resource<List<StatisticsEntity>>>
}