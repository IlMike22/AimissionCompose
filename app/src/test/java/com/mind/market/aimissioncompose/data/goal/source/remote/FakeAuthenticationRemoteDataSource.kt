package com.mind.market.aimissioncompose.data.goal.source.remote

import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.auth.data.model.User

class FakeAuthenticationRemoteDataSource: IAuthenticationRemoteDataSource { // TODO MIC move in authentication package
    override suspend fun createUser(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(
        email: String,
        password: String,
        onLoginResult: (User?, Throwable?) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun logoutUser(onUserLoggedOut: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun isUserAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUserData(): User {
        return User()
    }
}