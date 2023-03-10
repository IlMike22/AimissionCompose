package com.mind.market.aimissioncompose.statistics.data.implementation

import com.google.firebase.database.DatabaseReference
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_STATISTICS
import com.mind.market.aimissioncompose.data.common.FIREBASE_TABLE_USER
import com.mind.market.aimissioncompose.statistics.data.IStatisticsRemoteDataSource
import com.mind.market.aimissioncompose.statistics.data.dto.StatisticsEntityDto
import com.mind.market.aimissioncompose.statistics.data.mapper.toDomain
import com.mind.market.aimissioncompose.statistics.data.mapper.toDto
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity

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
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.value == null) {
                        onResult(
                            Throwable(message = "Table does not exist. Has to be created now."),
                            null
                        )
                    }
                }
            }
            .addOnSuccessListener { data ->
                data.getValue(StatisticsEntityDto::class.java)?.apply {
                    onResult(null, this.toDomain())
                }
            }
            .addOnFailureListener { error ->
                onResult(error, null)
            }
    }

    override suspend fun getAll(
        userId: String,
        onResult: (Throwable?, List<StatisticsEntity>?) -> Unit,
    ) {
        val entityDtos = mutableListOf<StatisticsEntityDto>()
        databaseReference
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_STATISTICS)
            .get()
            .addOnSuccessListener { data ->
                for (dataset in data.children) {
                    dataset.getValue(StatisticsEntityDto::class.java)?.let { entityDto ->
                        entityDtos.add(entityDto)
                    }
                }
                onResult(null, entityDtos.map { it.toDomain() })
            }
    }

    override suspend fun deleteAll(userId: String?, onResult: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun getByDate(
        month: Int,
        year: Int,
        userId: String,
        onResult: (Throwable?, StatisticsEntity?) -> Unit
    ) {
        val id = "$month$year"
        databaseReference
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_STATISTICS)
            .child(id)
            .get()
            .addOnSuccessListener { data ->
                val dtoEntity = data.getValue(StatisticsEntityDto::class.java)
                onResult(null, dtoEntity.toDomain())
            }
            .addOnFailureListener {
                onResult(it, null)
            }
    }

    override suspend fun update(entity: StatisticsEntityDto, userId: String) {
        databaseReference
            .child(FIREBASE_TABLE_USER)
            .child(userId)
            .child(FIREBASE_TABLE_STATISTICS)
            .child(entity.id)
            .setValue(entity)
    }
}