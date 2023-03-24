package com.mind.market.aimissioncompose.auth.data

import com.mind.market.aimissioncompose.auth.data.model.User

interface IAuthenticationRemoteDataSource {
    suspend fun createUser(
        email: String,
        password: String,
        onSignUpResult: (Throwable?, User?) -> Unit
    )

    suspend fun loginUser(
        email: String,
        password: String,
        onLoginResult: (User?, Throwable?) -> Unit
    )

    suspend fun logoutUser(onUserLoggedOut: () -> Unit)

    fun isUserAuthenticated(): Boolean
    fun getUserData(): User
}