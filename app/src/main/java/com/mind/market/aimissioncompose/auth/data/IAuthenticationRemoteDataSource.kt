package com.mind.market.aimissioncompose.auth.data

import com.mind.market.aimissioncompose.auth.data.model.User

interface IAuthenticationRemoteDataSource {
    suspend fun createUser(email: String, password: String)
    fun isUserAuthenticated(): Boolean
    fun getUserData(token: String): User
    fun loginUser(email: String, password: String)
}