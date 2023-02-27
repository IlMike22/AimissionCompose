package com.mind.market.aimissioncompose.auth.data

import com.mind.market.aimissioncompose.auth.data.model.User
import com.mind.market.aimissioncompose.auth.data.model.UserDto

class AuthenticationRepository(
    private val remoteDataSource: IAuthenticationRemoteDataSource,
    private val localDataSource: IAuthenticationLocalDataSource
) : IAuthenticationRepository {
    override suspend fun createUser(email: String, password: String) {
        remoteDataSource.createUser(email, password)
    }

    override suspend fun loginUser(
        email: String,
        password: String,
        onLoginResult: (User?, Throwable?) -> Unit
    ) {
        remoteDataSource.loginUser(email, password) { user, error ->
            if (error == null) {
                // success case
                onLoginResult(user, null)
            } else {
                // error case
                onLoginResult(null, error)
            }
        }
    }

    override suspend fun saveFirebaseUser(user: User) {
        localDataSource.saveFirebaseUser(user.toUserDto())
    }

    override suspend fun getFirebaseUser(id: String): User {
        return localDataSource.getFirebaseUser(id).toUser()
    }

    private fun User.toUserDto(): UserDto {
        return UserDto(
            tenantId = this.tenantId,
            name = this.name,
            email = this.email,
            id = null
        )
    }

    private fun UserDto.toUser(): User =
        User(
            tenantId = this.tenantId,
            name = this.name,
            email = this.email
        )
}