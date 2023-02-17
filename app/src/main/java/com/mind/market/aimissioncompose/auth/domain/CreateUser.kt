package com.mind.market.aimissioncompose.auth.domain

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRepository

class CreateUser(
    private val repository: IAuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        repository.createUser(email, password)
    }
}