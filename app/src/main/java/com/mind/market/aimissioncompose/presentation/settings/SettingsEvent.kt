package com.mind.market.aimissioncompose.presentation.settings

sealed interface SettingsEvent {
    data class HideDoneGoals(val hide: Boolean) : SettingsEvent
    data class ShowGoalOverdueDialog(val show: Boolean) : SettingsEvent
}