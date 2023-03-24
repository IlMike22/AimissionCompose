package com.mind.market.aimissioncompose.auth.domain

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRepository
import com.mind.market.aimissioncompose.auth.data.model.User

class CreateUserUseCase(
    private val repository: IAuthenticationRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        onResult: (Throwable?, User?) -> Unit
    ) {
        repository.createUser(email, password, onResult)
    }
}