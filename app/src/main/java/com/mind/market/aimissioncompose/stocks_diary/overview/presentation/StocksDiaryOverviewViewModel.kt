package com.mind.market.aimissioncompose.stocks_diary.overview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.IStocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksDiaryOverviewViewModel @Inject constructor(
    val repository: IStocksDiaryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(StocksDiaryOverviewState())
    val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            loadDiaries()
        }
    }

    fun onEvent(event: StocksDiaryOverviewEvent) {
        when (event) {
            StocksDiaryOverviewEvent.OnUpdateList -> {
                viewModelScope.launch {
                    loadDiaries()
                }
            }

            is StocksDiaryOverviewEvent.OnItemRemove -> {
                viewModelScope.launch {
                    repository.removeStocksDiary(event.item) { error ->
                        if (error != null) {
                            _state.update { it.copy(errorMessage = error.message) }
                        } else {
                            _state.update { it.copy(stockDiaries = _state.value.stockDiaries - event.item) }
                        }
                    }
                }
            }
        }
    }

    private suspend fun loadDiaries() {
        repository.getDiaries { error, diaries ->
            if (error != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unable to load your diaries"
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        stockDiaries = diaries
                    )
                }
            }
        }
    }
}