package com.mind.market.aimissioncompose.statistics.data.mapper

import com.mind.market.aimissioncompose.statistics.data.dto.StatisticsEntityDto
import com.mind.market.aimissioncompose.statistics.domain.models.StatisticsEntity
import java.time.LocalDateTime

fun StatisticsEntityDto?.toDomain(): StatisticsEntity {
    if (this == null) {
        return StatisticsEntity.EMPTY
    } else {
        return StatisticsEntity(
            id = this.id,
            title = this.title,
            amountGoalsCompleted = this.amountGoalsCompleted,
            amountGoalsCreated = this.amountGoalsCreated,
            amountGoalsNotCompleted = this.amountGoalsNotCompleted,
            amountGoalsStarted = this.amountGoalsStarted,
            month = this.month,
            year = this.year,
            lastUpdated = LocalDateTime.parse(this.lastUpdated),
            created = LocalDateTime.parse(this.created),
            grade = this.grade
        )
    }
}

fun StatisticsEntity.toDto(): StatisticsEntityDto =
    StatisticsEntityDto(
        title = this.title,
        amountGoalsCompleted = this.amountGoalsCompleted,
        amountGoalsCreated = this.amountGoalsCreated,
        amountGoalsNotCompleted = this.amountGoalsNotCompleted,
        amountGoalsStarted = this.amountGoalsStarted,
        month = this.month,
        year = this.year,
        lastUpdated = this.lastUpdated.toString(),
        created = this.created.toString(),
        grade = this.grade
    )