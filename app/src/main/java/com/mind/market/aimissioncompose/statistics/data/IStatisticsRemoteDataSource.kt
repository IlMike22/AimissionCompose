package com.mind.market.aimissioncompose.statistics.data

import com.mind.market.aimissioncompose.statistics.data.dto.StatisticsEntityDto
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity

interface IStatisticsRemoteDataSource {
    suspend fun insert(
        userId: String,
        entity: StatisticsEntity,
        onResult: (Boolean) -> Unit
    )

    suspend fun getAll(userId: String, onResult: (Throwable?, List<StatisticsEntity>?) -> Unit)
    suspend fun get(id: String, userId: String, onResult: (Throwable?, StatisticsEntity?) -> Unit)
    suspend fun deleteAll(userId: String? = null, onResult: (Boolean) -> Unit)
    suspend fun getByDate(
        month: Int,
        year: Int,
        userId: String,
        onResult: (Throwable?, StatisticsEntity?) -> Unit // TODO MIC we should always only have dtos in the remote ds, map it in repo not in ds to domain!
    )
    suspend fun update(entity: StatisticsEntityDto, userId: String)
}