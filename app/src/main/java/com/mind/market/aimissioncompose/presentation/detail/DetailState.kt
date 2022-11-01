package com.mind.market.aimissioncompose.presentation.detail

import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Priority

data class DetailState(
    val title: String = "",
    val description: String = "",
    val genre: Genre = Genre.UNKNOWN,
    val priority: Priority = Priority.UNKNOWN,
    val status: Status = Status.UNKNOWN
)
