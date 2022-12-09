package com.mind.market.aimissioncompose.presentation.settings

sealed interface SettingsEvent {
    object DuplicateGoals : SettingsEvent
    data class HideDoneGoals(val hide: Boolean) : SettingsEvent
}