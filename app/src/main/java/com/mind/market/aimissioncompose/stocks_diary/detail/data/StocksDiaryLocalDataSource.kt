package com.mind.market.aimissioncompose.stocks_diary.detail.data

import com.mind.market.aimissioncompose.stocks_diary.overview.data.IStocksDiaryDao
import com.mind.market.aimissioncompose.stocks_diary.overview.data.StocksDiaryDto

class StocksDiaryLocalDataSource(
    private val dao: IStocksDiaryDao
) : IStocksDiaryLocalDataSource {
    override fun addDiary(diary: StocksDiaryDto) {
        dao.insertStocksDiary(diary)
    }

    override fun addDiaries(diaries: List<StocksDiaryDto>) {
        diaries.forEach { diary ->
            dao.insertStocksDiary(diary)
        }
    }

    override suspend fun getDiaries(): List<StocksDiaryDto> {
        return dao.getDiaries()
    }

    override suspend fun removeDiary(diary: StocksDiaryDto) {
        dao.removeDiary(diary)
    }
}