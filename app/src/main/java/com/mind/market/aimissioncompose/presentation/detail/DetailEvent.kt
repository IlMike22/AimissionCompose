package com.mind.market.aimissioncompose.presentation.detail

import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.Status
import java.time.LocalDateTime

sealed class DetailEvent {
    data class OnTitleChanged(val title: String) : DetailEvent()
    data class OnDescriptionChanged(val description: String) : DetailEvent()
    data class OnPriorityChanged(val priority: Priority) : DetailEvent()
    data class OnStatusChanged(val status: Status) : DetailEvent()
    data class OnGenreChanged(val genre: Genre) : DetailEvent()
    data class OnFinishDateChanged(val finishDate: LocalDateTime) : DetailEvent()
    object OnSaveButtonClicked : DetailEvent()
}




