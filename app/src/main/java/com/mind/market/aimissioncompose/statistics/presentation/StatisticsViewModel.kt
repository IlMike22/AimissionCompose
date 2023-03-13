package com.mind.market.aimissioncompose.statistics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.CreateStatisticsGradeUseCase
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.GenerateStatisticsUseCase
import com.mind.market.aimissioncompose.statistics.domain.use_case.implementation.GetStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatistics: GetStatisticsUseCase,
    private val createStatisticsGrade: CreateStatisticsGradeUseCase,
    private val generateStatistics: GenerateStatisticsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(StatisticsState())
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), StatisticsState()
    )

    init {
        viewModelScope.launch {
        generateStatistics()

//            getStatistics().collect { response ->
//                when (response) {
//                    is Resource.Loading -> {
//                        _state.update { it.copy(isLoading = true) }
//                        delay(200) // TODO MIC just test, remove later
//                    }
//                    is Resource.Error -> _state.update {
//                        it.copy(
//                            isLoading = false,
//                            errorMessage = response.message
//                        )
//                    }
//                    is Resource.Success -> {
//                        response.data?.apply {
//                            val grades = createStatisticsGrade(this)
//                            _state.update {
//                                it.copy(
//                                    isLoading = false,
//                                    statisticsEntities = this,
//                                    grades = grades
//                                )
//                            }
//                        } ?: _state.update {
//                            it.copy(
//                                errorMessage = "Error. Response is null. No data."
//                            )
//                        }
//                    }
//                }
//            }
        }
    }

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            StatisticsEvent.OnCloseStatistics -> TODO()
            is StatisticsEvent.OnSortModeChanged -> Unit
        }
    }
}