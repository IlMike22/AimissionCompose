package com.mind.market.aimissioncompose.di

import com.mind.market.aimissioncompose.auth.data.*
import com.mind.market.aimissioncompose.auth.domain.CreateUserUseCase
import com.mind.market.aimissioncompose.auth.domain.LoginUserUseCase
import com.mind.market.aimissioncompose.auth.domain.StoreLocalUserUseCase
import com.mind.market.aimissioncompose.data.GoalRoomDatabase
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
    fun provideAuthenticationLocalDataSource(database: GoalRoomDatabase): IAuthenticationLocalDataSource {
        return AuthenticationLocalDataSource(database.authenticationDao())
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        remoteDataSource: IAuthenticationRemoteDataSource,
        localDataSource: IAuthenticationLocalDataSource
    ): IAuthenticationRepository {
        return AuthenticationRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @Provides
    @Singleton
    fun provideCreateUserUseCase(repository: IAuthenticationRepository): CreateUserUseCase {
        return CreateUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideStoreLocalUserUseCase(repository: IAuthenticationRepository): StoreLocalUserUseCase {
        return StoreLocalUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: IAuthenticationRepository): LoginUserUseCase {
        return LoginUserUseCase(repository)
    }
}