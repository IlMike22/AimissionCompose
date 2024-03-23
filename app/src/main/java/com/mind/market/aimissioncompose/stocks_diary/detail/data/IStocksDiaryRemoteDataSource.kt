package com.mind.market.aimissioncompose.stocks_diary.detail.data

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

interface IStocksDiaryRemoteDataSource {
    suspend fun getDiaries(userId: String): StocksDiaryResponse
    suspend fun addDiary(userId: String?, diary: StocksDiaryData, onResult: (Throwable?) -> Unit)
    suspend fun getDiaryForToday(userId:String, onResult: (StocksDiaryDomain?) -> Unit)
    suspend fun removeDiary(userId:String?, diary:StocksDiaryData): Throwable?
}