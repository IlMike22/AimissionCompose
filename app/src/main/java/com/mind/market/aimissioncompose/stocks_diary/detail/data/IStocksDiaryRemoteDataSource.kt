package com.mind.market.aimissioncompose.stocks_diary.detail.data

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

interface IStocksDiaryRemoteDataSource {
    suspend fun addDiary(userId: String?, diary: StocksDiaryData, onResult: (Throwable?) -> Unit)
    fun getDiaries(userId: String, onResult: (Throwable?, List<StocksDiaryDomain>) -> Unit) // CAUTION DOMAIN MODEL!!!
    suspend fun getDiaryForToday(userId:String, onResult: (StocksDiaryDomain?) -> Unit)
}