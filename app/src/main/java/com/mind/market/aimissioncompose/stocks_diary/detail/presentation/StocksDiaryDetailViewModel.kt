package com.mind.market.aimissioncompose.stocks_diary.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.IStocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
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
        }
    }

    private suspend fun addDiaryEntryIfNotExistsAlready(
        diary: StocksDiaryDomain
    ) {
        repository.getStocksDiaryOfToday { diaryOfToday ->
//            if (diaryOfToday == null || diary.createdDate != LocalDate.now()) { // TODO MIC just tmp. removed for testing with more items
                viewModelScope.launch {// TODO MIC ANTIPATTERN? How to avoid nested vm scopes??
                    repository.addDiary(diary) { error ->
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
                        }
                    }
                }
//            } else {
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
