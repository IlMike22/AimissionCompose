package com.mind.market.aimissioncompose.auth.presentation

sealed interface AuthenticationUiEvent {
    object NavigateToLandingPageAfterLogin : AuthenticationUiEvent
}