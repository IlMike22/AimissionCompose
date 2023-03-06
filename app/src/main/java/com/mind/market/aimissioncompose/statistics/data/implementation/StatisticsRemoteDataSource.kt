package com.mind.market.aimissioncompose.statistics.data.implementation

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_STATISTICS
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_USER
import com.mind.market.aimissioncompose.statistics.data.IStatisticsRemoteDataSource
import com.mind.market.aimissioncompose.statistics.data.mapper.toDto
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

class StatisticsRemoteDataSource(
    private val firebaseDatabase: DatabaseReference
) : IStatisticsRemoteDataSource {
    override suspend fun insert(
        userId: String,
        entity: StatisticsEntity,
        onResult: (Boolean) -> Unit
    ) {
        if (entity.id == null) {
            onResult(false)
        } else {
            firebaseDatabase
                .child(FIREBASE_TABLE_USER)
                .child(userId)
                .child(FIREBASE_TABLE_STATISTICS)
                .child(entity.id) // format: 032023 -> March 23
                .setValue(entity.toDto())
        }
    }

    override fun get(id: Int): Flow<Resource<StatisticsEntity>> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<Resource<List<StatisticsEntity>>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(onResult: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getByDate(month: Int, year: Int): Flow<Resource<StatisticsEntity>> {
        TODO("Not yet implemented")
    }

    override fun update(entity: StatisticsEntity): Flow<Resource<StatisticsEntity>> {
        TODO("Not yet implemented")
    }

}