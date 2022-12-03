package com.mind.market.aimissioncompose.presentation.landing_page.components

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface GoalUiEvent {
    data class ChangeStatusIcon(val newIcon: ImageVector) : GoalUiEvent
}
