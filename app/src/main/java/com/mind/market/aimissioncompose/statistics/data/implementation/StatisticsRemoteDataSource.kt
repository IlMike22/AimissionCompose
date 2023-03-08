package com.mind.market.aimissioncompose.statistics.data.implementation

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_STATISTICS
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_USER
import com.mind.market.aimissioncompose.statistics.data.IStatisticsRemoteDataSource
import com.mind.market.aimissioncompose.statistics.data.dto.StatisticsEntityDto
import com.mind.market.aimissioncompose.statistics.data.mapper.toDomain
import com.mind.market.aimissioncompose.statistics.data.mapper.toDto
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import kotlinx.coroutines.flow.Flow

class StatisticsRemoteDataSource(
    private val databaseReference: DatabaseReference
) : IStatisticsRemoteDataSource {
    override suspend fun insert(
        userId: String,
        entity: StatisticsEntity,
        onResult: (Boolean) -> Unit
    ) {
        if (entity.id.isBlank()) {
            onResult(false)
        } else {
            databaseReference
                .child(FIREBASE_TABLE_USER)
                .child(userId)
                .child(FIREBASE_TABLE_STATISTICS)
                .child(entity.id) // format: 32023 -> March 23, 112024 -> November 24
                .setValue(entity.toDto())
        }
    }

    override suspend fun get(
        id: String,
        userId: String,
        onResult: (Throwable?, StatisticsEntity?) -> Unit
    ) {
        databaseReference
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_STATISTICS)
            .child(id)
            .get()
            .addOnSuccessListener { data ->
                data.getValue(StatisticsEntityDto::class.java)?.apply {
                    onResult(null, this.toDomain())
                }
            }
            .addOnFailureListener {error ->
                onResult(error, null)
            }
    }

    override fun getAll(): Flow<Resource<List<StatisticsEntity>>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(onResult: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getByDate(month: Int, year: Int, userId: String): StatisticsEntity {
        TODO("Not yet implemented")
    }

    override suspend fun update(entity: StatisticsEntity): StatisticsEntity {
        TODO("Not yet implemented")
    }
}