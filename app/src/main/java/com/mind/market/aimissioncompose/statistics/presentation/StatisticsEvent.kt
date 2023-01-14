package com.mind.market.aimissioncompose.statistics.presentation

sealed class StatisticsEvent {
    object OnCloseStatistics : StatisticsEvent()
    data class OnSortModeChanged(val sortMode: SortMode) : StatisticsEvent()
}

enum class SortMode {
    NEWEST_FIRST,
    BEST_FIRST, // show month entities on top which had good grade
    WORST_FIRST // show month entities on top which had bad grade
}
