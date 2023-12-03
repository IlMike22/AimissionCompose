package com.mind.market.aimissioncompose.stocks_diary.detail.presentation

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

sealed interface StocksDiaryDetailEvent {
    data class OnSaveButtonClicked(val diary: StocksDiaryDomain): StocksDiaryDetailEvent
    object OnBackClicked: StocksDiaryDetailEvent
}