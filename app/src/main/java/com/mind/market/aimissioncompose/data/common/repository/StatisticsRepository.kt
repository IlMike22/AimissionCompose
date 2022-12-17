package com.mind.market.aimissioncompose.data.common.repository

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StatisticsRepository(
    private val goalDao: IGoalDao
) : IStatisticsRepository {
    override suspend fun getAmountGoalsForStatus(): Flow<Resource<Map<Status, Int>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val result = mutableMapOf<Status, Int>()
                result[Status.TODO] = goalDao.getAmountGoalsForStatus(Status.TODO)
                result[Status.IN_PROGRESS] = goalDao.getAmountGoalsForStatus(Status.IN_PROGRESS)
                result[Status.DONE] = goalDao.getAmountGoalsForStatus(Status.DONE)

                emit(Resource.Success(result))

            } catch (exception: Exception) {
                emit(Resource.Error(message = "Error fetching data from db"))
            }
        }

    }

    override fun getAmountGoalsByGenre(): Flow<Resource<Map<String, Int>>> {
        TODO("Not yet implemented")
    }
}