package com.mind.market.aimissioncompose.stocks_diary.detail.domain.models

import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import java.time.LocalDate

data class StocksDiaryDomain(
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.GOOD, // TODO MIC Mood is part of presentation..think about it
    val createdDate: LocalDate = LocalDate.now()
)