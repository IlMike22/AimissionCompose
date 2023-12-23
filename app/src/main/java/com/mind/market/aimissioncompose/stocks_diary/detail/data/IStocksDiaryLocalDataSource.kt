package com.mind.market.aimissioncompose.stocks_diary.detail.data

import com.mind.market.aimissioncompose.stocks_diary.overview.data.StocksDiaryDto

interface IStocksDiaryLocalDataSource {
    fun addDiary(diary:StocksDiaryDto)
    fun addDiaries(diaries: List<StocksDiaryDto>)
    suspend fun getDiaries(): List<StocksDiaryDto>
}