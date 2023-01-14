package com.mind.market.aimissioncompose.statistics.domain.models

import com.mind.market.aimissioncompose.statistics.data.dto.Grade
import java.time.LocalDateTime

data class StatisticsEntity(
    val id: Int,
    val title: String,
    val amountGoalsCompleted: Int,
    val amountGoalsCreated: Int,
    val amountGoalsStarted: Int,
    val amountGoalsNotCompleted: Int,
    val grade: Grade,
    val month: Int,
    val year: Int,
    val lastUpdated: LocalDateTime,
    val created: LocalDateTime
) {
    companion object {
        val EMPTY = StatisticsEntity(
            id = -1,
            title = "",
            amountGoalsCompleted = 0,
            amountGoalsCreated = 0,
            amountGoalsStarted = 0,
            amountGoalsNotCompleted = 0,
            grade = Grade.UNDEFINED,
            month = -1,
            year = -1,
            lastUpdated = LocalDateTime.now(),
            created = LocalDateTime.now()
        )
    }
}
