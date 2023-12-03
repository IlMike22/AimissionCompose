package com.mind.market.aimissioncompose.stocks_diary.overview.presentation

sealed interface StocksDiaryOverviewEvent {
    object OnUpdateList : StocksDiaryOverviewEvent
}