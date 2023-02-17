package com.mind.market.aimissioncompose.auth.presentation

sealed interface AuthenticationEvent {
    data class OnEmailChanged(val email: String) : AuthenticationEvent
    data class OnPasswordChanged(val password: String) : AuthenticationEvent
    object OnCreateNewUser : AuthenticationEvent
}