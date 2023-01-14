package com.mind.market.aimissioncompose.data.common.repository

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.flow.Flow

interface ICommonStatisticsRepository {
    suspend fun getAmountGoalsForStatus(): Flow<Resource<Map<Status, Int>>>
    fun getAmountGoalsByGenre(): Flow<Resource<Map<String, Int>>>
}