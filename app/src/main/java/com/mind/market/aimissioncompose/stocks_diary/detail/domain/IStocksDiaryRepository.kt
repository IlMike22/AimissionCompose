package com.mind.market.aimissioncompose.stocks_diary.detail.domain

import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IStocksDiaryRepository {
    suspend fun addDiary(diary: StocksDiaryDomain, onResult: (Throwable?) -> Unit)
    suspend fun getDiaries(onResult: (Throwable?, List<StocksDiaryDomain>) -> Unit)
    suspend fun getStocksDiaryOfToday(onResult: (StocksDiaryDomain?) -> Unit)
}