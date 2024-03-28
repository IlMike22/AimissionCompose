package com.mind.market.aimissioncompose.stocks_diary.detail.domain

import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryResponse
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

interface IStocksDiaryRepository {
    suspend fun addDiary(diary: StocksDiaryData): Throwable?
    suspend fun getDiaries(): StocksDiaryResponse
    suspend fun getStocksDiaryOfToday(): StocksDiaryDomain?
    suspend fun removeStocksDiary(diary: StocksDiaryData): Throwable?
}