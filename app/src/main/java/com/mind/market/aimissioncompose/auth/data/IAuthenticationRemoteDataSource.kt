package com.mind.market.aimissioncompose.auth.data

import com.mind.market.aimissioncompose.auth.data.model.User

interface IAuthenticationRemoteDataSource {
    suspend fun createUser(email: String, password: String)
    suspend fun loginUser(
        email: String,
        password: String,
        onLoginResult: (User?, Throwable?) -> Unit
    )

    fun isUserAuthenticated(): Boolean
    fun getUserData(token: String): User
}