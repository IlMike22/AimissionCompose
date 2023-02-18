package com.mind.market.aimissioncompose.auth.domain

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRepository

class LoginUser(private val repository: IAuthenticationRepository) {
    suspend operator fun invoke(email: String, password: String) {
        repository.loginUser(email, password)
    }
}