package com.mind.market.aimissioncompose.auth.data

class AuthenticationRepository(
    private val remoteDataSource: IAuthenticationRemoteDataSource
) : IAuthenticationRepository {
    override suspend fun createUser(email: String, password: String) {
        remoteDataSource.createUser(email, password)
    }
}