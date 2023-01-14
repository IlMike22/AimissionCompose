package com.mind.market.aimissioncompose.statistics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.use_case.IStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val useCase: IStatisticsUseCase
) : ViewModel() {

    var state by mutableStateOf(StatisticsState())
        private set

    init {
        viewModelScope.launch {
            useCase.getStatistics().collect { response ->
                when (response) {
                    is Resource.Error -> TODO()
                    is Resource.Loading -> TODO()
                    is Resource.Success -> TODO()
                }
            }
        }
    }

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            StatisticsEvent.OnCloseStatistics -> TODO()
            is StatisticsEvent.OnSortModeChanged -> Unit
        }
    }
}