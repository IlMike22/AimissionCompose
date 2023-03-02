package com.mind.market.aimissioncompose.auth.domain

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRepository

class LogoutUserUseCase(
    private val repository: IAuthenticationRepository
) {
    suspend operator fun invoke() {
        repository.logoutUser()
    }
}