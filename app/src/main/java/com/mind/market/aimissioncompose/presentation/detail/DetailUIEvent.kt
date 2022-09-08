package com.mind.market.aimissioncompose.presentation.detail

sealed class DetailUIEvent() {
    data class ShowValidationResult(val statusCode: Int? = null) : DetailUIEvent()
    object HideKeyboard : DetailUIEvent()
    object NavigateToLandingPage : DetailUIEvent()
    object NavigateToSettings : DetailUIEvent()
    object NavigateToInfo : DetailUIEvent()
}