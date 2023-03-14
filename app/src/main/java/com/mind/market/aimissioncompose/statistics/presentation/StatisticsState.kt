package com.mind.market.aimissioncompose.statistics.presentation

import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsGrade

data class StatisticsState(
    val isLoading: Boolean = false,
    val statisticsEntities: List<StatisticsEntity> = emptyList(),
    val errorMessage: String? = null
)
