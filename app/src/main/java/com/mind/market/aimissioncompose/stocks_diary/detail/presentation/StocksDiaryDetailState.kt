package com.mind.market.aimissioncompose.stocks_diary.detail.presentation

import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain

data class StocksDiaryDetailState(
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val stocksDiary: StocksDiaryDomain = StocksDiaryDomain(),
    val isNavigateBack: Boolean = false,
    val isDiaryAlreadyExist: Boolean = false
)

enum class Mood {
    GOOD,
    OKAY,
    BAD,
    UNDEFINED
}
