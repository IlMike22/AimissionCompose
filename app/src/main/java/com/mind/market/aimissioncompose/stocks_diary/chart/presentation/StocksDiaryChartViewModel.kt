package com.mind.market.aimissioncompose.stocks_diary.chart.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StocksDiaryChartViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(StocksDiaryChartState())
    val state = _state.asStateFlow()

    init {

    }

    fun onEvent(event: StocksDiaryChartEvent) {

    }
}