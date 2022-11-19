package com.mind.market.aimissioncompose.data

import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.data.dto.GoalDto
import com.mind.market.aimissioncompose.domain.models.Goal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Status.toStatusData(): String {
    return when (this) {
        Status.TODO -> "TODO"
        Status.IN_PROGRESS -> "IN_PROGRESS"
        Status.DONE -> "DONE"
        Status.DEPRECATED -> "DEPRECATED"
        Status.UNKNOWN -> "UNKNOWN"
    }
}

fun GoalDto.toGoal(): Goal {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return Goal(
        id = this.id,
        title = this.title,
        description = this.description,
        creationDate = LocalDateTime.parse(this.creationDate),
        changeDate = LocalDateTime.parse(this.changeDate),
        isRepeated = this.isRepeated,
        genre = this.genre,
        status = this.status,
        priority = this.priority,
        finishDate = if (this.finishDate.isNotBlank()) LocalDateTime.parse(this.finishDate) else LocalDateTime.now()
    )
}

fun Goal.toGoalDto(): GoalDto =
    GoalDto(
        id = this.id,
        title = this.title,
        description = this.description,
        creationDate = this.creationDate.toString(),
        changeDate = this.changeDate.toString(),
        isRepeated = this.isRepeated,
        genre = this.genre,
        status = this.status,
        priority = this.priority,
        finishDate = this.finishDate.toString()
    )
