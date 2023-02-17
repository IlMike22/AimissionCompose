package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.auth.data.AuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.auth.data.AuthenticationRepository
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.auth.data.IAuthenticationRepository
import com.mind.market.aimissioncompose.auth.domain.CreateUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthenticationRemoteDataSource(): IAuthenticationRemoteDataSource {
        return AuthenticationRemoteDataSource()
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(dataSource: IAuthenticationRemoteDataSource): IAuthenticationRepository {
        return AuthenticationRepository(
            remoteDataSource = dataSource
        )
    }

    @Provides
    @Singleton
    fun provideCreateUserUseCase(repository: IAuthenticationRepository): CreateUser {
        return CreateUser(repository)
    }
}