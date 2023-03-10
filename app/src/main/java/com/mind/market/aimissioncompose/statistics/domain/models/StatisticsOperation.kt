package com.mind.market.aimissioncompose.statistics.domain.models

import com.mind.market.aimissioncompose.domain.models.Goal

sealed interface StatisticsOperation {
    data class Add(val goal: Goal) : StatisticsOperation
    data class Delete(val goal: Goal) : StatisticsOperation
    data class Update(val oldGoal: Goal, val newGoal: Goal) : StatisticsOperation
}