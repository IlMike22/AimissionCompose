package com.mind.market.aimissioncompose.stocks_diary.overview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toStocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.IStocksDiaryRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StocksDiaryOverviewViewModel @Inject constructor(
    val repository: IStocksDiaryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(StocksDiaryOverviewState())
    val state = _state.asStateFlow()
    private var removedItem: StocksDiaryDomain? = null

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
                    removedItem = event.item
                    val throwable = repository.removeStocksDiary(event.item.toStocksDiaryData())
                    if (throwable != null) {
                        _state.update {
                            it.copy(
                                errorMessage = throwable.message
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                stockDiaries = repository.getDiaries().diaries?.map { it.toDomain() }?: emptyList(),
                                showUndoSnackbar = true
                            )
                        }
                    }
                }
            }

            StocksDiaryOverviewEvent.OnOpenChartView -> Unit
            StocksDiaryOverviewEvent.OnUndoItemRemove -> {
                viewModelScope.launch {
                    if (removedItem == null) {
                        return@launch
                    }

                    val error = repository.addDiary(
                        (removedItem as StocksDiaryDomain)
                            .toStocksDiaryData()
                    )
                    if (error != null) {
                        _state.update {
                            it.copy(
                                errorMessage = error.message
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                stockDiaries = repository.getDiaries().diaries?.map { it.toDomain() }?: emptyList(),
                                showUndoSnackbar = false
                            )
                        }
                    }
                }
            }

            StocksDiaryOverviewEvent.OnDismissSnackbar -> _state.update {
                it.copy(showUndoSnackbar = false)
            }
        }
    }

    private suspend fun loadDiaries() {
        val response = repository.getDiaries()
        if (response.error != null) {
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
                    stockDiaries = response.diaries?.map { it.toDomain() } ?: emptyList(),
                    currentMonth = LocalDate.now().monthValue,
                    currentYear = LocalDate.now().year
                )
            }
        }
    }
}