package com.mind.market.aimissioncompose.presentation.detail

import com.mind.market.aimissioncompose.domain.models.Goal

data class DetailState(
    val goal: Goal = Goal.EMPTY,
    val ctaButtonText: String = "",
    val errorMessage:String? = null
)


//    val title: String = "",
//    val description: String = "",
//    val genre: Genre = Genre.UNKNOWN,
//    val priority: Priority = Priority.UNKNOWN,
//    val status: Status = Status.UNKNOWN,
//    val createdDate: LocalDateTime = LocalDateTime.now(),
//    val currentDate: LocalDateTime = LocalDateTime.now(),
//    val finishDate: LocalDate = LocalDate.now(),
//    val isRepeated: Boolean = false
//)
