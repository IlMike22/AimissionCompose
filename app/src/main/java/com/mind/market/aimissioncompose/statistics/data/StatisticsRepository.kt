package com.mind.market.aimissioncompose.statistics.data

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.data.mapper.toDto
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.repository.IStatisticsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class StatisticsRepository(
    private val localDataSource: IStatisticsEntityDao,
    private val remoteDataSource: IStatisticsRemoteDataSource,
    private val authRemoteDataSource: IAuthenticationRemoteDataSource
) : IStatisticsRepository {
    override suspend fun getStatisticsEntity(
        id: String,
        onResult: (Throwable?, StatisticsEntity?) -> Unit
    ) {
        remoteDataSource.get(id, getFirebaseUserId(), onResult)
    }

    override suspend fun getStatisticsEntityByDate(month: Int, year: Int, onResult: (Throwable?, StatisticsEntity?) -> Unit) {
        remoteDataSource.getByDate(month, year, getFirebaseUserId(), onResult)
//        return localDataSource.getStatisticsEntityByDate(month, year).toDomain()
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
    ) {
        remoteDataSource.update(entity.toDto(), getFirebaseUserId())
    }


    override fun getStatisticsEntities(): Flow<Resource<List<StatisticsEntity>>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            remoteDataSource.getAll(getFirebaseUserId()) { error, entities ->
                if (error != null) {
                    trySend(Resource.Loading(false))
                    trySend(Resource.Error(message = "An error occurred while reading the database. Details: ${error.message}"))
                    return@getAll
                }

                entities.apply {
                    trySend(Resource.Loading(false))
                    trySend(Resource.Success(this ?: emptyList()))
                }
            }
            awaitClose {}
        }
    }

    private fun getFirebaseUserId(): String = authRemoteDataSource.getUserData().id
}