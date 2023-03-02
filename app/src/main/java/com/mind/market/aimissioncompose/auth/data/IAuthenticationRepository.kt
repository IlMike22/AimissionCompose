package com.mind.market.aimissioncompose.auth.data

import com.mind.market.aimissioncompose.auth.data.model.User

interface IAuthenticationRepository {
    suspend fun createUser(email: String, password: String)
    suspend fun loginUser(
        email: String,
        password: String,
        onLoginResult: (User?, Throwable?) -> Unit
    )

    suspend fun logoutUser()

    suspend fun saveFirebaseUser(user: User)

    suspend fun getFirebaseUser(id: String): User
}