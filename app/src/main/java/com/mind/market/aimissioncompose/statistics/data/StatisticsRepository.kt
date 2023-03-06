package com.mind.market.aimissioncompose.statistics.data

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.data.mapper.toDomain
import com.mind.market.aimissioncompose.statistics.data.mapper.toDto
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StatisticsRepository(
    private val localDataSource: IStatisticsEntityDao,
    private val remoteDataSource: IStatisticsRemoteDataSource,
    private val authRemoteDataSource: IAuthenticationRemoteDataSource
) : IStatisticsRepository {
    override suspend fun getStatisticsEntity(id: Int): StatisticsEntity {
        return localDataSource.getStatisticsEntity(id).toDomain()
    }

    override suspend fun getStatisticsEntityByDate(month: Int, year: Int): StatisticsEntity {
        return localDataSource.getStatisticsEntityByDate(month, year).toDomain()
    }

    override suspend fun insertStatisticsEntity(
        entity: StatisticsEntity,
        onResult: (Boolean) -> Unit,
        operation: GoalReadWriteOperation
    ) {
        return if (operation == GoalReadWriteOperation.LOCAL_DATABASE) {
            localDataSource.insert(entity.toDto())
        } else {
            remoteDataSource.insert(getFirebaseUserId(), entity, onResult)
        }

    }

    override suspend fun updateStatisticEntity(
        entity: StatisticsEntity,
        operation: GoalReadWriteOperation
    ) {
        if (operation == GoalReadWriteOperation.LOCAL_DATABASE) {
            return localDataSource.update(entity.toDto())
        }
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

    private fun getFirebaseUserId():String = authRemoteDataSource.getUserData().id
}