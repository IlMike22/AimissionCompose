package com.mind.market.aimissioncompose.stocks_diary.chart.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StocksDiaryChartViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val currentMonth: Int = checkNotNull(savedStateHandle[ARGUMENT_CURRENT_MONTH])
    private val currentYear: Int = checkNotNull(savedStateHandle[ARGUMENT_CURRENT_YEAR])

    private val _state = MutableStateFlow(StocksDiaryChartState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                monthName = "Current month and year: $currentMonth $currentYear"
            )
        }
    }

    fun onEvent(event: StocksDiaryChartEvent) {

    }

    companion object {
        private const val ARGUMENT_CURRENT_MONTH = "currentMonth"
        private const val ARGUMENT_CURRENT_YEAR = "currentYear"

    }
}