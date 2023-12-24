package com.mind.market.aimissioncompose.stocks_diary.chart.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.stocks_diary.chart.domain.IStocksDiaryChartRepository
import com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper.toDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StocksDiaryChartViewModel @Inject constructor(
    private val repository: IStocksDiaryChartRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val currentMonth: Int = checkNotNull(savedStateHandle[ARGUMENT_CURRENT_MONTH])
    private val currentYear: Int = checkNotNull(savedStateHandle[ARGUMENT_CURRENT_YEAR])

    private val _state = MutableStateFlow(StocksDiaryChartState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val diaries = repository.getStocksDiariesFromDatabase()
            if (diaries.isNotEmpty()) {
                diaries.map { it.toDomain() }.run {
                    createMoodInformation(this)
                    _state.update {
                        it.copy(
                            diaries = this,
                            monthName = "Current month and year: $currentMonth $currentYear"
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: StocksDiaryChartEvent) {

    }

    private fun createMoodInformation(diaries: List<StocksDiaryDomain>) {
        val goodMood = diaries.filter { it.mood == Mood.GOOD }.run {
            this.size
        }
        val averageMood = diaries.filter { it.mood == Mood.OKAY }.run {
            this.size
        }
        val badMood = diaries.filter { it.mood == Mood.BAD }.run {
            this.size
        }

        _state.update {
            it.copy(
                moodInformation = MoodInformation(
                    badMoodCount = badMood,
                    averageMoodCount = averageMood,
                    goodMoodCount = goodMood
                )
            )
        }
    }

    companion object {
        private const val ARGUMENT_CURRENT_MONTH = "currentMonth"
        private const val ARGUMENT_CURRENT_YEAR = "currentYear"

    }
}