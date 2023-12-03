package com.mind.market.aimissioncompose.stocks_diary.overview.presentation

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

data class StocksDiaryOverviewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val stockDiaries: List<StocksDiaryDomain> = emptyList()
)
