package com.mind.market.aimissioncompose.stocks_diary.detail.domain.models

import android.os.Parcelable
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.time.LocalDate

@Parcelize
data class StocksDiaryDomain(
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.GOOD, // TODO MIC Mood is part of presentation..think about it
    val createdDate: LocalDate = LocalDate.now(),
    val stocksTraded: List<StocksTradingItem> = emptyList(),
) : Parcelable

@Parcelize
data class StocksTradingItem(
    val name: String = "",
    val amount: Int = -1,
    val pricePerStock: BigDecimal = BigDecimal(""),
    val reason: String = "",
    val type: TradingType = TradingType.BOUGHT
): Parcelable

enum class TradingType {
    BOUGHT, SOLD
}