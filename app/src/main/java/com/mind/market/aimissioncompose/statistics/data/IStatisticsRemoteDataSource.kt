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

    fun get(id: Int): Flow<Resource<StatisticsEntity>>
    fun getAll(): Flow<Resource<List<StatisticsEntity>>>
    suspend fun deleteAll(onResult: (Boolean) -> Unit)
    fun getByDate(month: Int, year: Int): Flow<Resource<StatisticsEntity>>
    fun update(entity: StatisticsEntity): Flow<Resource<StatisticsEntity>>
}