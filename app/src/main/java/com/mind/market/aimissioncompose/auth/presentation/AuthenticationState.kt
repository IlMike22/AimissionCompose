package com.mind.market.aimissioncompose.auth.presentation

import com.mind.market.aimissioncompose.auth.data.model.User

data class AuthenticationState(
    val email:String = "",
    val password:String ="",
    val user:User = User.EMPTY, // TODO MIC don`t use data model here! create domain model
    val isUserAuthenticated: Boolean = false
)