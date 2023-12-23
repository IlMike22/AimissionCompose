package com.mind.market.aimissioncompose.stocks_diary.chart.presentation

import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import java.time.LocalDate

data class StocksDiaryChartState(
    val monthName: String = "",
    val plots: Map<Mood, List<LocalDate>> = emptyMap(),

)
