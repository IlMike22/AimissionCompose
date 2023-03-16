package com.mind.market.aimissioncompose.auth.presentation

import com.mind.market.aimissioncompose.auth.data.model.User
import com.mind.market.aimissioncompose.auth.utils.AuthenticationValidationErrorStatus

data class AuthenticationUiState(
    val email: String = "",
    val password: String = "",
    val user: User = User.EMPTY, // TODO MIC don`t use data model here! create domain model
    val isUserAuthenticated: Boolean = false,
    val toastMessage: String? = null,
    val isLoading: Boolean = false,
    val validationErrorStatus: AuthenticationValidationErrorStatus? = null
)