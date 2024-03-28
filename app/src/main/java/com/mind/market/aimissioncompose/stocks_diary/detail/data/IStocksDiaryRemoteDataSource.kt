package com.mind.market.aimissioncompose.stocks_diary.detail.data

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

interface IStocksDiaryRemoteDataSource {
    suspend fun getDiaries(userId: String): StocksDiaryResponse
    suspend fun addDiary(userId: String?, diary: StocksDiaryData): Throwable?
    suspend fun getDiaryForToday(userId: String): StocksDiaryDomain?
    suspend fun removeDiary(userId: String?, diary: StocksDiaryData): Throwable?
}