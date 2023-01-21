package com.mind.market.aimissioncompose.statistics.data

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.data.mapper.toDomain
import com.mind.market.aimissioncompose.statistics.data.mapper.toDto
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StatisticsRepository(
    private val localDataSource: IStatisticsEntityDao
) : IStatisticsRepository {
    override suspend fun getStatisticsEntity(id: Int): StatisticsEntity {
        return localDataSource.getStatisticsEntity(id).toDomain()
    }

    override suspend fun getStatisticsEntityByDate(month: Int, year: Int): StatisticsEntity {
        return localDataSource.getStatisticsEntityByDate(month, year).toDomain()
    }

    override suspend fun insertStatisticsEntity(entity: StatisticsEntity) {
        return localDataSource.insert(entity.toDto())
    }

    override suspend fun updateStatisticEntity(entity: StatisticsEntity) {
        return localDataSource.update(entity.toDto())
    }

    //TODO MIC do not use Flow two times. It is not needed?
    override fun getStatisticsEntities(): Flow<Resource<Flow<List<StatisticsEntity>>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val entities = localDataSource.getStatisticsEntities()
                entities
                    .map { entityDto ->
                        entityDto.map { it.toDomain() }
                    }
                    .apply {
                        emit(Resource.Loading(false))
                        emit(Resource.Success(this))
                    }
            } catch (exception: Throwable) {
                emit(Resource.Loading(false))
                emit(Resource.Error(message = "An error occured while reading the database. Details: ${exception.message}"))
            }

        }
    }
}