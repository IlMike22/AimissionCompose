package com.mind.market.aimissioncompose.auth.domain

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRepository
import com.mind.market.aimissioncompose.auth.data.model.User

class StoreLocalUserUseCase(
    private val repository: IAuthenticationRepository
) {
    suspend operator fun invoke(user: User) {
        repository.saveFirebaseUser(user)
    }
}