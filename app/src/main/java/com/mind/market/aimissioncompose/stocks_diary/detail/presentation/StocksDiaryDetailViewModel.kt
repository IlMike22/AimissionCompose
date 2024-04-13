package com.mind.market.aimissioncompose.stocks_diary.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toStocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.IStocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksDiaryDetailViewModel @Inject constructor(
    private val repository: IStocksDiaryRepository // no use case needed here..
) : ViewModel() {
    private val _state = MutableStateFlow(StocksDiaryDetailState())
    val state: StateFlow<StocksDiaryDetailState> = _state

    fun onEvent(event: StocksDiaryDetailEvent) {
        when (event) {
            is StocksDiaryDetailEvent.OnBackClicked -> _state.update { it.copy(isNavigateBack = true) }
            is StocksDiaryDetailEvent.OnSaveButtonClicked -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    addDiaryEntryIfNotExistsAlready(event.diary)
                }
            }

            is StocksDiaryDetailEvent.OnMoodChanged -> {
                _state.update { it.copy(stocksDiary = it.stocksDiary.copy(mood = event.newMood)) }
            }

            is StocksDiaryDetailEvent.OnTitleChanged -> {
                _state.update { it.copy(stocksDiary = it.stocksDiary.copy(title = event.title)) }
            }

            is StocksDiaryDetailEvent.OnDescriptionChanged -> {
                _state.update { it.copy(stocksDiary = it.stocksDiary.copy(description = event.description))}}

            is StocksDiaryDetailEvent.OnStockTradingChange -> _state.update {
                it.copy(
                    stocksDiary = it.stocksDiary.copy(
                        stocksTraded = it.stocksDiary.stocksTraded + event.stockTradingDetail
                    )
                )
            }
        }
    }

    private suspend fun addDiaryEntryIfNotExistsAlready(
        diary: StocksDiaryDomain
    ) {
        val diaryOfToday = repository.getStocksDiaryOfToday()
//            if (diaryOfToday == null || diary.createdDate != LocalDate.now()) { // TODO MIC just tmp. removed for testing with more items

        val error = repository.addDiary(diary.toStocksDiaryData())
        if (error != null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = error.message,
                )
            }
        } else {
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    isNavigateBack = true
                )
            }
//            } else { // TODO do NOT remove. Needed later..
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = "You have already added an item for today."
//                    )
//                }
//            }
        }
    }
}
