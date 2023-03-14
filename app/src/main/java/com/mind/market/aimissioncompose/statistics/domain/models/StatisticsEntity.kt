package com.mind.market.aimissioncompose.statistics.domain.models

import java.time.LocalDateTime

data class StatisticsEntity(
    val id: String,
    val title: String,
    val data: StatisticData,
    val grade: StatisticsGrade,
    val month: Int,
    val year: Int,
    val lastUpdated: LocalDateTime,
    val created: LocalDateTime
) {
    companion object {
        val EMPTY = StatisticsEntity(
            id = "000000",
            title = "",
            data = StatisticData.EMPTY,
            grade = StatisticsGrade.UNDEFINED,
            month = -1,
            year = -1,
            lastUpdated = LocalDateTime.now(),
            created = LocalDateTime.now()
        )
    }
}

data class StatisticData(
    val totalAmount: Int,
    val totalGoalsToDo: Int,
    val totalGoalsInProgress: Int,
    val totalGoalsCompleted: Int,
    val totalGoalsDeprecated: Int
) {
    companion object {
        val EMPTY = StatisticData(
            totalAmount = 0,
            totalGoalsCompleted = 0,
            totalGoalsDeprecated = 0,
            totalGoalsInProgress = 0,
            totalGoalsToDo = 0
        )
    }
}
