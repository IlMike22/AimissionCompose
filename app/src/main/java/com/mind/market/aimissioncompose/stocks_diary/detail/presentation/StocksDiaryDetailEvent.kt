package com.mind.market.aimissioncompose.stocks_diary.detail.presentation

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StockTradingDetail
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

sealed interface StocksDiaryDetailEvent {
    data class OnSaveButtonClicked(val diary: StocksDiaryDomain): StocksDiaryDetailEvent
    object OnBackClicked: StocksDiaryDetailEvent
    data class OnMoodChanged(val newMood: Mood): StocksDiaryDetailEvent
    data class OnTitleChanged(val title: String): StocksDiaryDetailEvent
    data class OnDescriptionChanged(val description: String): StocksDiaryDetailEvent
    data class OnStockTradingChange(val stockTradingDetail: StockTradingDetail): StocksDiaryDetailEvent
}