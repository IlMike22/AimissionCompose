package com.mind.market.aimissioncompose.stocks_diary.overview.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryData

@Entity(tableName = "stocks_diary_table")
data class StocksDiaryDto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0,
    val diaryId: Int = -1,
    val title: String = "",
    val description: String = "",
    val mood: Int = 0,
    val createdDate: String = ""
)
