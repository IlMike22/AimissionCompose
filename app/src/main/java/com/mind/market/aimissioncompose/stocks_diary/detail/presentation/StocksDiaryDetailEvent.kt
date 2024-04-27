package com.mind.market.aimissioncompose.stocks_diary.detail.presentation

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import java.math.BigDecimal

sealed interface StocksDiaryDetailEvent {
    data class OnSaveButtonClicked(val diary: StocksDiaryDomain) : StocksDiaryDetailEvent
    object OnBackClicked : StocksDiaryDetailEvent
    data class OnMoodChanged(val newMood: Mood) : StocksDiaryDetailEvent
    data class OnTitleChanged(val title: String) : StocksDiaryDetailEvent
    data class OnDescriptionChanged(val description: String) : StocksDiaryDetailEvent
    data class OnAddNewTradingItem(
        val name: String,
        val reason: String,
        val amount: Int,
        val pricePerStock: BigDecimal
    ) : StocksDiaryDetailEvent
}