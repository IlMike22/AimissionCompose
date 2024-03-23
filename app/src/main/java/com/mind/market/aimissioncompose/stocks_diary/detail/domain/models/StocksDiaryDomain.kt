package com.mind.market.aimissioncompose.stocks_diary.detail.domain.models

import android.os.Parcelable
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class StocksDiaryDomain(
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.GOOD, // TODO MIC Mood is part of presentation..think about it
    val createdDate: LocalDate = LocalDate.now(),
//    val stocksSold: StocksInformation = StocksInformation(),
//    val stocksBought: StocksInformation = StocksInformation()
) : Parcelable

data class StocksInformation(
    val name: String = "",
    val amount: Int = -1,
    val pricePerStock: Double = 0.0,
    val reason: String = ""
)