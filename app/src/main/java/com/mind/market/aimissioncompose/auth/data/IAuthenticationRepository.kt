package com.mind.market.aimissioncompose.auth.data

interface IAuthenticationRepository {
    suspend fun createUser(email:String, password:String)
    suspend fun loginUser(email:String, password:String)
}