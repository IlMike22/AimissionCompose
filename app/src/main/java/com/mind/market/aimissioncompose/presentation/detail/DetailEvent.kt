package com.mind.market.aimissioncompose.presentation.detail

import com.example.aimissionlite.models.domain.Status
import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Priority

sealed class DetailEvent {
    data class OnTitleChanged(val newTitle: String) : DetailEvent()
    data class OnDescriptionChanged(val newDescription: String) : DetailEvent()
    data class OnPriorityChanged(val newPriority: Priority) : DetailEvent()
    data class OnStatusChanged(val newStatus: Status) : DetailEvent()
    data class OnGenreChanged(val newGenre: Genre) : DetailEvent()
}




