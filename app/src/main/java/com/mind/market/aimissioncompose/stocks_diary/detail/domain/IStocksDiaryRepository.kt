package com.mind.market.aimissioncompose.stocks_diary.detail.domain

import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

interface IStocksDiaryRepository {
    suspend fun addDiary(diary: StocksDiaryData, onResult: (Throwable?) -> Unit)
    suspend fun getDiaries(onResult: (Throwable?, List<StocksDiaryData>) -> Unit)
    suspend fun getStocksDiaryOfToday(onResult: (StocksDiaryDomain?) -> Unit)
    suspend fun removeStocksDiary(diary: StocksDiaryData, onResult: (Throwable?) -> Unit)
}