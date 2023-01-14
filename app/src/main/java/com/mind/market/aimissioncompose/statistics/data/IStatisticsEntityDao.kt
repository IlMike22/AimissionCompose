package com.mind.market.aimissioncompose.statistics.data

import androidx.room.*
import com.mind.market.aimissioncompose.statistics.data.dto.Grade
import com.mind.market.aimissioncompose.statistics.data.dto.StatisticsEntityDto
import kotlinx.coroutines.flow.Flow

@Dao
interface IStatisticsEntityDao {
    @Query("SELECT * FROM statistics_table")
    fun getStatisticsEntities(): Flow<List<StatisticsEntityDto>>

    @Query("SELECT * FROM statistics_table WHERE id = :id")
    suspend fun getStatisticsEntity(id: Int): StatisticsEntityDto

    @Query("UPDATE statistics_table SET grade = :grade WHERE id = :id")
    suspend fun updateGrade(id: Int, grade: Grade)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(statisticsEntity: StatisticsEntityDto)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(statisticsEntity: StatisticsEntityDto)

    @Delete
    suspend fun deleteStatisticsEntity(statisticsEntity: StatisticsEntityDto)

    @Query("DELETE FROM statistics_table")
    suspend fun deleteAll()
}