package com.mind.market.aimissioncompose.statistics.domain.repository

import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

interface IStatisticsRepository {
    suspend fun getEntity(id: String, onResult: (Throwable?, StatisticsEntity?) -> Unit)
    suspend fun getEntityByDate(
        month: Int,
        year: Int,
        onResult: (Throwable?, StatisticsEntity?) -> Unit
    )

    suspend fun insertEntity(
        entity: StatisticsEntity,
        onResult: (Boolean) -> Unit,
        operation: GoalReadWriteOperation
    )

    suspend fun updateEntity(entity: StatisticsEntity)
    fun getEntities(): Flow<Resource<List<StatisticsEntity>>>
}