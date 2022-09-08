package com.mind.market.aimissioncompose.domain.common.uiEvent

sealed class UiEvent {
    object NavigateUp:UiEvent()
    data class ShowSnackbar(val text:String):UiEvent()

}
