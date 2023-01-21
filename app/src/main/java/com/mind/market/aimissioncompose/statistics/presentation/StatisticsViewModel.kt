package com.mind.market.aimissioncompose.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.use_case.IStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val useCase: IStatisticsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(StatisticsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), StatisticsState()
    )

    init {
        viewModelScope.launch {
            useCase.getStatistics().collect { response ->
                when (response) {
                    is Resource.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.message
                        )
                    }
                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                        delay(2000) // TODO MIC just test
                    }
                    is Resource.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            statisticsEntities = response.data?.first() ?: emptyList() // TODO atm only one element is observed
                        )
                    }
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