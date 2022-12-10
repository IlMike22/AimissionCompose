package com.mind.market.aimissioncompose.domain.models

import java.time.LocalDate
import java.time.LocalDateTime

data class Goal(
    val id: Int,
    val title: String,
    val description: String,
    val creationDate: LocalDateTime,
    val changeDate: LocalDateTime,
    val isRepeated: Boolean,
    val genre: Genre,
    val status: Status,
    val priority: Priority,
    val finishDate: LocalDate
) {
    companion object {
        val EMPTY = Goal(
            id = -1,
            title = "",
            description = "",
            creationDate = LocalDateTime.now(),
            changeDate = LocalDateTime.now(),
            isRepeated = false,
            genre = Genre.UNKNOWN,
            status = Status.UNKNOWN,
            priority = Priority.UNKNOWN,
            finishDate = LocalDate.now()
        )
    }
}







