package com.mind.market.aimissioncompose.stocks_diary.chart.data

import com.mind.market.aimissioncompose.stocks_diary.chart.domain.IStocksDiaryChartRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.data.IStocksDiaryLocalDataSource
import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toStocksDiaryData

class StocksDiaryChartRepository(
    private val localDataSource: IStocksDiaryLocalDataSource
) : IStocksDiaryChartRepository {
    override suspend fun getStocksDiariesFromDatabase(): List<StocksDiaryData> {
        return localDataSource.getDiaries().map { it.toStocksDiaryData() }
    }
}