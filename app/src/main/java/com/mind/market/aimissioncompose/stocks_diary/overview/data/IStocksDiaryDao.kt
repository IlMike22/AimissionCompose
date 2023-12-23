package com.mind.market.aimissioncompose.stocks_diary.overview.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mind.market.aimissioncompose.data.dto.GoalDto

@Dao
interface IStocksDiaryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStocksDiary(diary: StocksDiaryDto)

    @Query("SELECT * FROM stocks_diary_table")
    suspend fun getDiaries(): List<StocksDiaryDto>
}