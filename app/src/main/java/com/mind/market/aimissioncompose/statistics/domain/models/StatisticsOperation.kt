package com.mind.market.aimissioncompose.statistics.domain.models

import com.mind.market.aimissioncompose.domain.models.Goal

sealed class StatisticsOperation() {
    data class AddGoal(val newGoal: Goal) : StatisticsOperation()
}