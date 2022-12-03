package com.mind.market.aimissioncompose.presentation.landing_page.components

import com.example.aimissionlite.models.domain.Status

sealed interface GoalEvent {
    class OnGoalUiStatusChanged(val newStatus: Status) : GoalEvent
}
