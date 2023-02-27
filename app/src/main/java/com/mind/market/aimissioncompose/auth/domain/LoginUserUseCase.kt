package com.mind.market.aimissioncompose.auth.domain

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRepository
import com.mind.market.aimissioncompose.auth.data.model.User

class LoginUserUseCase(private val repository: IAuthenticationRepository) {
    suspend operator fun invoke(
        email: String,
        password: String,
        onLoginResult: (User?, Throwable?) -> Unit
    ) {
        repository.loginUser(email, password, onLoginResult)
    }
}