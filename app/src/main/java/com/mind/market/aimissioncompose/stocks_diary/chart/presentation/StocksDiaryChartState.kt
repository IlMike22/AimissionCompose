package com.mind.market.aimissioncompose.stocks_diary.chart.presentation

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import java.time.LocalDate

data class StocksDiaryChartState(
    val monthName: String = "",
    val plots: Map<Mood, List<LocalDate>> = emptyMap(),
    val diaries: List<StocksDiaryDomain> = emptyList(),
    val moodInformation: MoodInformation = MoodInformation(),
    val highestSoldOut: StocksDetail = StocksDetail(),
    val highestBuy: StocksDetail = StocksDetail()
)

data class StocksDetail(
    val name: String = "",
    val summary: Double = 0.0
)

data class MoodInformation(
    val badMoodCount: Int = 0,
    val averageMoodCount: Int = 0,
    val goodMoodCount: Int = 0,
)
