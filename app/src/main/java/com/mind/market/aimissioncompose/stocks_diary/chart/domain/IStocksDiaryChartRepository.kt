package com.mind.market.aimissioncompose.stocks_diary.chart.domain

import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryData

interface IStocksDiaryChartRepository {
    suspend fun getStocksDiariesFromDatabase(): List<StocksDiaryData>
}