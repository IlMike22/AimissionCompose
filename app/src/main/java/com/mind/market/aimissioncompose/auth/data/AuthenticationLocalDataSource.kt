package com.mind.market.aimissioncompose.auth.data

import com.mind.market.aimissioncompose.auth.data.model.UserDto

class AuthenticationLocalDataSource(
    private val authDao:IAuthenticationDao
):IAuthenticationLocalDataSource {
    override suspend fun getFirebaseUser(id:String): UserDto {
        return authDao.getFirebaseUser(id)
    }

    override suspend fun saveFirebaseUser(user: UserDto) {
       authDao.insert(user)
    }
}