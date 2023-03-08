package com.mind.market.aimissioncompose.statistics.data

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

interface IStatisticsRemoteDataSource {
    suspend fun insert(
        userId: String,
        entity: StatisticsEntity,
        onResult: (Boolean) -> Unit
    )

    fun getAll(): Flow<Resource<List<StatisticsEntity>>>
    suspend fun get(id: String, userId: String, onResult: (Throwable?, StatisticsEntity?) -> Unit)
    suspend fun deleteAll(onResult: (Boolean) -> Unit)
    suspend fun getByDate(month: Int, year: Int, userId: String): StatisticsEntity
    suspend fun update(entity: StatisticsEntity): StatisticsEntity
}