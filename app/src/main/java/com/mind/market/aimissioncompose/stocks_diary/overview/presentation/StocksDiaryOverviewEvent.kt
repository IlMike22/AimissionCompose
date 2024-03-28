package com.mind.market.aimissioncompose.stocks_diary.overview.presentation

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

sealed interface StocksDiaryOverviewEvent {
    object OnUpdateList : StocksDiaryOverviewEvent
    data class OnItemRemove(val item: StocksDiaryDomain) : StocksDiaryOverviewEvent
    object OnOpenChartView : StocksDiaryOverviewEvent
    object OnUndoItemRemove : StocksDiaryOverviewEvent
    object OnDismissSnackbar: StocksDiaryOverviewEvent
}