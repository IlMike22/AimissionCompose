package com.mind.market.aimissioncompose.auth.data

import com.mind.market.aimissioncompose.auth.data.model.User
import com.mind.market.aimissioncompose.auth.data.model.UserDto

interface IAuthenticationLocalDataSource {
    suspend fun getFirebaseUser(id:String):UserDto
    suspend fun saveFirebaseUser(user: UserDto)
}